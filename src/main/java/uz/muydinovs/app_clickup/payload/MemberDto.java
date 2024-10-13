package uz.muydinovs.app_clickup.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class MemberDto {
    private UUID id;

    private UUID RoleId;

    private String addType;//ADD, EDIT, DELETE
}
