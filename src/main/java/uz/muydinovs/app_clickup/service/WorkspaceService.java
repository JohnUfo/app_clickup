package uz.muydinovs.app_clickup.service;

import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.payload.WorkspaceRoleDto;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {
    ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user);

    ApiResponse editWorkspace(Long workspaceId, WorkspaceDto workspaceDto, User user);

    ApiResponse changeOwnerWorkspace(Long workspaceId, UUID ownerId, User user);

    ApiResponse deleteWorkspace(Long id);

    ApiResponse addOrEditOrRemoveMemberFromWorkspace(Long workspaceId, MemberDto memberDto);

    ApiResponse joinToWorkspace(Long id, User user);

    ApiResponse addRoleToWorkspace(Long workspaceId, WorkspaceRoleDto workspaceRoleDto, User user);

    List<MemberDto> getMemberAndGuests(Long workspaceId);

    List<WorkspaceDto> getMyWorkspaces(User user);

    ApiResponse addOrRemovePermission(WorkspaceRoleDto workspaceRoleDto);
}
