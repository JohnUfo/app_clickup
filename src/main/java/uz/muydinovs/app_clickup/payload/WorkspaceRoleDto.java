package uz.muydinovs.app_clickup.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.muydinovs.app_clickup.entity.enums.AddType;
import uz.muydinovs.app_clickup.entity.enums.WorkspacePermissionName;
import uz.muydinovs.app_clickup.entity.enums.WorkspaceRoleName;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRoleDto {

    private UUID id;
    private String name;
    private WorkspaceRoleName workspaceRoleName;
    private WorkspacePermissionName permissionName;
    private AddType addType;
}
