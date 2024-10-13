package uz.muydinovs.app_clickup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.entity.Workspace;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.MemberDto;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.security.CurrentUser;
import uz.muydinovs.app_clickup.service.WorkspaceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDto workspaceDto, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspace(workspaceDto,user);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> addWorkspace(@PathVariable Long id, @RequestBody WorkspaceDto workspaceDto) {
        ApiResponse apiResponse = workspaceService.editWorkspace(id,workspaceDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PutMapping("/changeOwner/{id}")
    public HttpEntity<?> changeOwnerWorkspace(@PathVariable Long id, @RequestParam UUID ownerId) {
        ApiResponse apiResponse = workspaceService.changeOwnerWorkspace(id,ownerId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        ApiResponse apiResponse = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrRemove/{id}")
    public HttpEntity<?> addOrEditWorkspace(@PathVariable Long id, @RequestBody MemberDto memberDto) {
        ApiResponse apiResponse = workspaceService.addOrEditWorkspace(id,memberDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
