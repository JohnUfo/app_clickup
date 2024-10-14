package uz.muydinovs.app_clickup.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.muydinovs.app_clickup.entity.enums.AddType;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {
    public MemberDto(UUID id, UUID roleId, AddType addType) {
        this.id = id;
        this.roleId = roleId;
        this.addType = addType;
    }

    private UUID id;

    private String fullName;

    private String email;

    private String roleName;

    private Timestamp lastActive;

    private UUID roleId;

    private AddType addType;//ADD, EDIT, DELETE
}
