package uz.muydinovs.app_clickup.payload;

import lombok.Data;
import uz.muydinovs.app_clickup.entity.enums.AddType;

import java.util.UUID;

@Data
public class MemberDto {
    private UUID id;

    private UUID roleId;

    private AddType addType;//ADD, EDIT, DELETE
}
