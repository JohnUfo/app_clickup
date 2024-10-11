package uz.muydinovs.app_clickup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.LoginDto;
import uz.muydinovs.app_clickup.payload.RegisterDto;
import uz.muydinovs.app_clickup.security.JwtProvider;
import uz.muydinovs.app_clickup.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;
    AuthenticationManager authenticationManager;
    JwtProvider jwtProvider;

    @Autowired
    @Lazy
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam("email") String email, @RequestParam("emailCode") String emailCode) {
        ApiResponse apiResponse = authService.verifyEmail(email, emailCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail());

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Invalid email or password", false));
        }
    }
}
