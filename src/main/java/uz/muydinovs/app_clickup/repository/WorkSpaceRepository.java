package uz.muydinovs.app_clickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.muydinovs.app_clickup.entity.Workspace;

import java.util.UUID;

public interface WorkSpaceRepository extends JpaRepository<Workspace,Long> {
    boolean existsByOwnerIdAndName(UUID ownerId, String name);
}
