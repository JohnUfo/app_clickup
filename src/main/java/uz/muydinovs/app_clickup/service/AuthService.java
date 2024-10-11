package uz.muydinovs.app_clickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.muydinovs.app_clickup.entity.User;
import uz.muydinovs.app_clickup.entity.enums.SystemRoleName;
import uz.muydinovs.app_clickup.payload.ApiResponse;
import uz.muydinovs.app_clickup.payload.LoginDto;
import uz.muydinovs.app_clickup.payload.RegisterDto;
import uz.muydinovs.app_clickup.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JavaMailSender javaMailSender;
    AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public ApiResponse registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ApiResponse("This user already exists", false);
        }


        User user = new User(
                registerDto.getFullName(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                SystemRoleName.SYSTEM_ROLE_USER,
                String.valueOf(new Random().nextInt(999999)).substring(0, 4)
        );

        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Verify your email!", true);
    }


    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("hvjjj83@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Verify email");
            mailMessage.setText(emailCode);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (emailCode.equals(user.getEmailCode())) {

                user.setEnabled(true);
                user.setEmailCode(null);
                userRepository.save(user);
                return new ApiResponse("Account verified!", true);
            }
            return new ApiResponse("Invalid email code", false);
        }
        return new ApiResponse("User not found", false);
    }
}
