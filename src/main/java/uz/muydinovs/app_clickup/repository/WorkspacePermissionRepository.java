package uz.muydinovs.app_clickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.muydinovs.app_clickup.entity.WorkspacePermission;
import uz.muydinovs.app_clickup.entity.enums.WorkspacePermissionName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacePermissionRepository extends JpaRepository<WorkspacePermission, UUID> {
    Optional<WorkspacePermission> findByWorkspaceRoleIdAndPermission(UUID workspaceRole_id, WorkspacePermissionName permission);

    List<WorkspacePermission> findAllByWorkspaceRole_NameAndWorkspaceRole_WorkspaceId(String workspaceRole_name, Long workspaceRole_workspace_id);
}
