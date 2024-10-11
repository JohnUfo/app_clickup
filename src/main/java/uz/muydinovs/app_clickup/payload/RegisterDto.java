package uz.muydinovs.app_clickup.payload;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RegisterDto {
    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
