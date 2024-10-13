package uz.muydinovs.app_clickup.service;

import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;

import java.util.UUID;

public interface WorkspaceService {
    ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user);

    ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto);

    ApiResponse changeOwnerWorkspace(Long id, UUID ownerId);

    ApiResponse deleteWorkspace(Long id);

    ApiResponse addOrEditOrRemoveMemberFromWorkspace(Long id, MemberDto memberDto);

}
