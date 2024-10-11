package uz.muydinovs.app_clickup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.muydinovs.app_clickup.entity.enums.WorkspacePermissionName;
import uz.muydinovs.app_clickup.entity.template.AbsUUIDEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WorkspacePermission extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private WorkspaceRole workspaceRole; //OWNER, ADMIN

    @Enumerated(EnumType.STRING)
    private WorkspacePermissionName permission; //add member, remove member
}
