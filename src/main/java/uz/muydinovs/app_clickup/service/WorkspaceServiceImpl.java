package uz.muydinovs.app_clickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.muydinovs.app_clickup.entity.*;
import uz.muydinovs.app_clickup.entity.enums.AddType;
import uz.muydinovs.app_clickup.entity.enums.WorkspacePermissionName;
import uz.muydinovs.app_clickup.entity.enums.WorkspaceRoleName;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.payload.WorkspaceRoleDto;
import uz.muydinovs.app_clickup.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    WorkSpaceRepository workSpaceRepository;
    UserRepository userRepository;
    AttachmentRepository attachmentRepository;
    WorkspaceUserRepository workspaceUserRepository;
    WorkspaceRoleRepository workspaceRoleRepository;
    WorkspacePermissionRepository workspacePermissionRepository;

    @Autowired
    @Lazy
    public WorkspaceServiceImpl(WorkSpaceRepository workSpaceRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, WorkspaceUserRepository workspaceUserRepository, WorkspaceRoleRepository workspaceRoleRepository, WorkspacePermissionRepository workspacePermissionRepository) {
        this.workSpaceRepository = workSpaceRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.workspaceUserRepository = workspaceUserRepository;
        this.workspaceRoleRepository = workspaceRoleRepository;
        this.workspacePermissionRepository = workspacePermissionRepository;
    }

    @Override
    public ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user) {
        if (workSpaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDto.getName())) {
            return new ApiResponse("Workspace is already exist", false);
        }
        Workspace workspace = new Workspace(workspaceDto.getName(), workspaceDto.getColor(), user, workspaceDto.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("attachment")));

        workSpaceRepository.save(workspace);

        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_OWNER.name(), null));

        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));

        WorkspacePermissionName[] workspacePermissionNames = WorkspacePermissionName.values();

        List<WorkspacePermission> workspacePermissions = new ArrayList<>();

        for (WorkspacePermissionName workspacePermissionName : workspacePermissionNames) {
            WorkspacePermission workspacePermissionOwner = new WorkspacePermission(ownerRole, workspacePermissionName);
            workspacePermissions.add(workspacePermissionOwner);
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissions.add(new WorkspacePermission(adminRole, workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissions.add(new WorkspacePermission(memberRole, workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissions.add(new WorkspacePermission(guestRole, workspacePermissionName));
            }
        }

        workspacePermissionRepository.saveAll(workspacePermissions);

        workspaceUserRepository.save(new WorkspaceUser(workspace, user, ownerRole, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

        return new ApiResponse("Workspace added", true);
    }

    @Override
    public ApiResponse editWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = workSpaceRepository.findById(workspaceDto.getId()).orElseThrow(ResourceNotFoundException::new);

        workspace.setName(workspaceDto.getName());
        workspace.setColor(workspaceDto.getColor());
        workspace.setAvatar(workspaceDto.getAvatarId() != null ?
                attachmentRepository.findById(workspaceDto.getAvatarId())
                        .orElseThrow(ResourceNotFoundException::new) : null);

        workSpaceRepository.save(workspace);
        return new ApiResponse("Workspace editeed", true);
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long workspaceId, UUID newOwnerId, User user) {
        if (newOwnerId.equals(user.getId())) return new ApiResponse("User ids are same", false);

        Optional<Workspace> optionalWorkspace = workSpaceRepository.findById(workspaceId);
        if (optionalWorkspace.isPresent() && optionalWorkspace.get().getOwner().equals(user)) {
            Workspace workspace = optionalWorkspace.get();
            Optional<User> optionalUser = userRepository.findById(newOwnerId);
            if (optionalUser.isPresent()) {
                workspace.setOwner(optionalUser.get());
                workSpaceRepository.save(workspace);
                return new ApiResponse("Workspace owner is changed", true);
            }
            return new ApiResponse("User not found", false);
        }
        return new ApiResponse("workspace or user id is not correct", false);
    }

    @Override
    public ApiResponse deleteWorkspace(Long id) {
        if (!workSpaceRepository.existsById(id)) {
            return new ApiResponse("Workspace not found", false);
        }
        workSpaceRepository.deleteById(id);
        return new ApiResponse("Workspace deleted", true);
    }

    //TODO to invite email for user
    @Override
    public ApiResponse addOrEditOrRemoveMemberFromWorkspace(Long workspaceId, MemberDto memberDto) {
        switch (memberDto.getAddType()) {
            case ADD -> {
                WorkspaceUser workspaceUser = new WorkspaceUser(workSpaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("id")), userRepository.findById(memberDto.getId()).orElseThrow(() -> new ResourceNotFoundException("id")), workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")), new Timestamp(System.currentTimeMillis()), null);
                workspaceUserRepository.save(workspaceUser);
            }
            case EDIT -> {
                WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, memberDto.getId()).orElseGet(WorkspaceUser::new);
                workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));

                workspaceUserRepository.save(workspaceUser);
            }
            case REMOVE -> {
                workspaceUserRepository.deleteByWorkspaceIdAndUserId(workspaceId, memberDto.getId());
            }
        }
        return new ApiResponse("Success", true);
    }

    @Override
    public ApiResponse joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDate_joined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Success", true);
        }

        return new ApiResponse("Failed", false);
    }

    @Override
    public ApiResponse addRoleToWorkspace(Long workspaceId, WorkspaceRoleDto workspaceRoleDto, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            if (workspaceUser.getWorkspaceRole().getName().equals(WorkspaceRoleName.ROLE_OWNER.name()) || workspaceUser.getWorkspaceRole().getName().equals(WorkspaceRoleName.ROLE_ADMIN.name())) {
                Optional<Workspace> optionalWorkspace = workSpaceRepository.findById(workspaceId);
                if (optionalWorkspace.isPresent()) {
                    WorkspaceRole workspaceRole = workspaceRoleRepository.save(new WorkspaceRole(optionalWorkspace.get(), workspaceRoleDto.getName(), workspaceRoleDto.getExtendsRole()));
                    List<WorkspacePermission> workspacePermissions = workspacePermissionRepository.findAllByWorkspaceRole_NameAndWorkspaceRole_WorkspaceId(workspaceRoleDto.getExtendsRole().name(), workspaceId);

                    List<WorkspacePermission> newWorkspacePermissions = new ArrayList<>();
                    for (WorkspacePermission workspacePermission : workspacePermissions) {
                        WorkspacePermission newWorkspacePermission = new WorkspacePermission(
                                workspaceRole,
                                workspacePermission.getPermission()

                        );
                        newWorkspacePermissions.add(newWorkspacePermission);
                    }
                    workspacePermissionRepository.saveAll(newWorkspacePermissions);
                    return new ApiResponse("Successfully " + workspaceRoleDto.getName() + " added to workspace role", true);
                }
                return new ApiResponse("workspace is not exist", false);
            }
            return new ApiResponse("Only owner and admin can add new role to workspace", false);
        }
        return new ApiResponse("wrong user id", false);
    }

    @Override
    public List<MemberDto> getMemberAndGuests(Long workspaceId) {
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByWorkspaceId(workspaceId);
        return workspaceUsers.stream().map(this::mapWorkspaceUserToMemberDto).toList();
    }

    @Override
    public List<WorkspaceDto> getMyWorkspaces(User user) {
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByUserId(user.getId());
        return workspaceUsers.stream().map(workspaceUser -> mapWorkspaceUserToWorkspaceDto(workspaceUser.getWorkspace())).toList();
    }

    @Override
    public ApiResponse addOrRemovePermission(WorkspaceRoleDto workspaceRoleDto) {
        WorkspaceRole workspaceRole = workspaceRoleRepository.findById(workspaceRoleDto.getId()).orElseThrow(() -> new ResourceNotFoundException("workspaceRole"));
        Optional<WorkspacePermission> optionalWorkspacePermission = workspacePermissionRepository.findByWorkspaceRoleIdAndPermission(workspaceRole.getId(), workspaceRoleDto.getPermissionName());
        if (workspaceRoleDto.getAddType().equals(AddType.ADD)) {
            if (optionalWorkspacePermission.isPresent()) return new ApiResponse("Permission already added", false);
            WorkspacePermission workspacePermission = new WorkspacePermission(workspaceRole, workspaceRoleDto.getPermissionName());
            workspacePermissionRepository.save(workspacePermission);
            return new ApiResponse("Permission added", true);

        } else if (workspaceRoleDto.getAddType().equals(AddType.REMOVE)) {
            if (optionalWorkspacePermission.isPresent()) {
                workspacePermissionRepository.delete(optionalWorkspacePermission.get());
                return new ApiResponse("Permission removed", true);
            }
            return new ApiResponse("no object found", false);
        }
        return new ApiResponse("wrong permission", false);
    }

    public MemberDto mapWorkspaceUserToMemberDto(WorkspaceUser workspaceUser) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(workspaceUser.getUser().getId());
        memberDto.setFullName(workspaceUser.getUser().getFullName());
        memberDto.setEmail(workspaceUser.getUser().getEmail());
        memberDto.setRoleName(workspaceUser.getWorkspaceRole().getName());
        memberDto.setLastActive(workspaceUser.getUser().getLastActiveTime());
        return memberDto;
    }

    public WorkspaceDto mapWorkspaceUserToWorkspaceDto(Workspace workspace) {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(workspace.getId());
        workspaceDto.setName(workspace.getName());
        workspaceDto.setInitialLetter(workspace.getInitialLetter());
        workspaceDto.setOwnerId(workspace.getOwner().getId());
        workspaceDto.setColor(workspace.getColor());
        workspaceDto.setAvatarId(workspace.getAvatar() == null ? null : workspace.getAvatar().getId());
        return workspaceDto;
    }
}


