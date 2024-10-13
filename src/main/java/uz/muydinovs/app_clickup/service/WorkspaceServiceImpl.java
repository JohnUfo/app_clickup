package uz.muydinovs.app_clickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.muydinovs.app_clickup.entity.*;
import uz.muydinovs.app_clickup.entity.enums.WorkspacePermissionName;
import uz.muydinovs.app_clickup.entity.enums.WorkspaceRoleName;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    public ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto) {
        //name,color,avatar
        if (!workSpaceRepository.existsById(id)) {
            return new ApiResponse("Workspace not found", false);
        }
        Workspace workspace = workSpaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("workspace"));
        workspace.setName(workspaceDto.getName());
        workspace.setColor(workspaceDto.getColor());
        workSpaceRepository.save(workspace);
        return null;
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        return null;
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
    public ApiResponse addOrEditOrRemoveMemberFromWorkspace(Long id, MemberDto memberDto) {
        switch (memberDto.getAddType()) {
            case ADD -> {
                WorkspaceUser workspaceUser = new WorkspaceUser(
                        workSpaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                        userRepository.findById(memberDto.getId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                        workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                        new Timestamp(System.currentTimeMillis()),
                        null
                );
                workspaceUserRepository.save(workspaceUser);
            }
            case EDIT -> {
                WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, memberDto.getId()).orElseGet(WorkspaceUser::new);
                workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));

                workspaceUserRepository.save(workspaceUser);
            }
            case REMOVE -> {
                workspaceUserRepository.deleteByWorkspaceIdAndUserId(id, memberDto.getId());
            }
        }
        return new ApiResponse("Success", true);
    }
}
