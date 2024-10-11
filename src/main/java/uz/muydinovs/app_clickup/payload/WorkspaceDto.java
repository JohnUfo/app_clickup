package uz.muydinovs.app_clickup.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WorkspaceDto {
    @NotNull
    private String name;

    @NotNull
    private String color;

    private UUID avatarId;
}
