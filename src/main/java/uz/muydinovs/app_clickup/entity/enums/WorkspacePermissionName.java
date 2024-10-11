package uz.muydinovs.app_clickup.entity.enums;


import java.util.Arrays;
import java.util.List;

public enum WorkspacePermissionName {
    CAN_ADD_MEMBER("ADD/REMOVE MEMBERS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_REMOVE_MEMBERS("CAN_REMOVE_MEMBERS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_EDIT_WORKSPACE("CAN_EDIT_WORKSPACE", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_ADD_GUEST("CAN_ADD_GUEST", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_SEE_TIME_ESTIMATED("CAN_SEE_TIME_ESTIMATED", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_SEE_TIME_SPENT("CAN_SEE_TIME_SPENT", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_CREATE_SPACES("CAN_CREATE_SPACES", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_CREATE_FOLDER("CAN_CREATE_FOLDER", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_CREATE_LISTS("CAN_CREATE_LISTS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_DELETE_COMMENTS("CAN_DELETE_COMMENTS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_DELETE_ITEMS("CAN_DELETE_ITEMS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_EDIT_DESCRIPTION("CAN_EDIT_DESCRIPTION", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_ADD_LIST_STATUES("CAN_ADD_LIST_STATUES", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_EDIT_LIST_STATUES("CAN_EDIT_LIST_STATUES", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_EDIT_TEAM("CAN_EDIT_TEAM", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_EDIT_TEAM_OWNER("CAN_EDIT_TEAM_OWNER", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_MANAGE_TAGS("CAN_MANAGE_TAGS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_SHARE("CAN_SHARE", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_MANAGE_STATUES("CAN_MANAGE_STATUES", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_SEE_TEAM_MEMBERS("CAN_SEE_TEAM_MEMBERS", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_ADD_ROLE("CAN_ADD_ROLE", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN)),

    CAN_CHANGE_PERMISSION("CAN_CHANGE_PERMISSION", Arrays.asList(WorkspaceRoleName.ROLE_OWNER, WorkspaceRoleName.ROLE_ADMIN));

    public String name;
    public List<WorkspaceRoleName> workspaceRoleNames;

    WorkspacePermissionName(String name, List<WorkspaceRoleName> workspaceRoleNames) {
        this.name = name;
        this.workspaceRoleNames = workspaceRoleNames;
    }

}
