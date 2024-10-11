package uz.muydinovs.app_clickup.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.LoginDto;
import uz.muydinovs.app_clickup.payload.RegisterDto;
import uz.muydinovs.app_clickup.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    @Lazy
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam("email") String email, @RequestParam("emailCode") String emailCode) {
        ApiResponse apiResponse = authService.verifyEmail(email,emailCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @PostMapping("/login")
//    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
//        ApiResponse apiResponse = authService.login(loginDto);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
//    }
}
