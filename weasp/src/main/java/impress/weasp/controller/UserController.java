package impress.weasp.controller;

import impress.weasp.controller.dto.perfil.user.UserProfileUpdateDTO;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.User;
import impress.weasp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String jwtToken) {
        String email = jwtTokenProvider.validateToken(jwtToken);
        User user = userService.getUserProfile(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UserProfileUpdateDTO dto, @RequestHeader("Authorization") String jwtToken) {
        Optional<User> email = userService.findByEmail(jwtToken);
        User updatedUser = userService.updateUserProfile(dto, email.get().getEmail());
        return ResponseEntity.ok(updatedUser);
    }
}

