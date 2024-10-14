package uz.muydinovs.app_clickup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.payload.WorkspaceRoleDto;
import uz.muydinovs.app_clickup.repository.WorkSpaceRepository;
import uz.muydinovs.app_clickup.security.CurrentUser;
import uz.muydinovs.app_clickup.service.WorkspaceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDto workspaceDto, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspace(workspaceDto, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("editWorkspace")
    public HttpEntity<?> editWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        ApiResponse apiResponse = workspaceService.editWorkspace(workspaceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/changeOwner/{workspaceId}")
    public HttpEntity<?> changeOwnerWorkspace(@PathVariable Long workspaceId, @RequestParam UUID ownerId, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.changeOwnerWorkspace(workspaceId, ownerId,user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        ApiResponse apiResponse = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrRemoveMemberFromWorkspace/{workspaceId}")
    public HttpEntity<?> addOrEditOrRemoveMemberFromWorkspace(@PathVariable Long workspaceId, @RequestBody MemberDto memberDto) {
        ApiResponse apiResponse = workspaceService.addOrEditOrRemoveMemberFromWorkspace(workspaceId, memberDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/join")
    public HttpEntity<?> joinToWorkspace(@RequestParam Long id, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.joinToWorkspace(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addRole/{workspaceId}")//workspaceId
    public HttpEntity<?> addRoleToWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceRoleDto workspaceRoleDto,@CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addRoleToWorkspace(workspaceId, workspaceRoleDto,user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //shu workspaceda bogan odamla kora olishi kere buning uchun
    // current user qoshish kere uni worksapce id sini olish kere
    @GetMapping("/member/{workspaceId}")
    public HttpEntity<?> getMemberAndGuests(@PathVariable Long workspaceId,@CurrentUser User user) {
        ApiResponse memberAndGuests = workspaceService.getMemberAndGuests(workspaceId, user);
        return ResponseEntity.ok(memberAndGuests);
    }

    @GetMapping
    public HttpEntity<?> getMyWorkspaces(@CurrentUser User user) {
        List<WorkspaceDto> workspaces = workspaceService.getMyWorkspaces(user);
        return ResponseEntity.ok(workspaces);
    }

    @PutMapping("/addOrRemovePermission")
    public HttpEntity<?> addOrRemovePermissionToRole(@RequestBody WorkspaceRoleDto workspaceRoleDto){
        ApiResponse apiResponse = workspaceService.addOrRemovePermission(workspaceRoleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }
}
