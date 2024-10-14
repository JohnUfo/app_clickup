package uz.muydinovs.app_clickup.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String color;

    private UUID avatarId;

    private UUID ownerId;

    private String initialLetter;

    public WorkspaceDto(Long id, String name, String color, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.ownerId = ownerId;
    }

    public WorkspaceDto(String name, String color, UUID avatarId) {
        this.name = name;
        this.color = color;
        this.avatarId = avatarId;
    }
}
