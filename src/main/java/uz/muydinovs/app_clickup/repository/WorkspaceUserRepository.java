package uz.muydinovs.app_clickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.entity.Workspace;
import uz.muydinovs.app_clickup.entity.WorkspaceUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {
    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(Long workspaceId, UUID userId);

    @Transactional
    @Modifying
    void deleteByWorkspaceIdAndUserId(Long workspaceId, UUID userId);

    List<WorkspaceUser> findAllByWorkspaceId(Long workspaceId);

    List<WorkspaceUser> findAllByUserId(UUID id);
}
