package impress.weasp.service;




import impress.weasp.controller.dto.perfil.user.UserProfileUpdateDTO;
import impress.weasp.model.User;
import impress.weasp.model.domain.USER_ROLE;
import impress.weasp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public User createUser(String email, String password, String name, USER_ROLE role) {
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email já registrado.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return userRepository.save(user);
    }
    public User updateUserProfile(UserProfileUpdateDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (dto.name() != null) user.setName(dto.name());
        if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));

        return userRepository.save(user);
    }

    public User getUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }
}

