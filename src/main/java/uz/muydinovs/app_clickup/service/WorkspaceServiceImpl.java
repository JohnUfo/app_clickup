package uz.muydinovs.app_clickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.muydinovs.app_clickup.entity.Attachment;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.entity.Workspace;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.WorkspaceDto;
import uz.muydinovs.app_clickup.repository.AttachmentRepository;
import uz.muydinovs.app_clickup.repository.WorkSpaceRepository;

import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    WorkSpaceRepository workSpaceRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Override
    public ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user) {
        if (workSpaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDto.getName())) {
            return new ApiResponse("Workspace is already exist", false);
        }
        Workspace workspace = new Workspace(
                workspaceDto.getName(),
                workspaceDto.getColor(),
                user,
                workspaceDto.getAvatarId() == null ? null :
                        attachmentRepository.findById(workspaceDto.getAvatarId())
                                .orElseThrow(() -> new ResourceNotFoundException("attachment")));

        workSpaceRepository.save(workspace);

        return new ApiResponse("Workspace added", true);
    }

    @Override
    public ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto) {
        return null;
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        return null;
    }

    @Override
    public ApiResponse deleteWorkspace(Long id) {
        return null;
    }
}
