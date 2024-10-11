package uz.muydinovs.app_clickup.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
