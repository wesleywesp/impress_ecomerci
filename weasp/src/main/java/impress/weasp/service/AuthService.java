package impress.weasp.service;

import impress.weasp.controller.dto.auth.*;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.Admin;
import impress.weasp.model.PendingAdmin;
import impress.weasp.model.User;
import impress.weasp.model.domain.RoleProvider;
import impress.weasp.model.domain.USER_ROLE;
import impress.weasp.repository.AdminRepository;
import impress.weasp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    /**
     * Login de usuários (CUSTOMER ou ADMIN)
     */
    public AuthResponse login(LoginRequestDTO req) {
        RoleProvider userOrAdmin;

        // Primeiro tenta encontrar o usuário como CUSTOMER
        Optional<User> user = userRepository.findByEmail(req.email());
        if (user.isPresent()) {
            userOrAdmin = user.get();
        } else {
            // Caso contrário, tenta encontrar como ADMIN
            Admin admin = adminRepository.findByEmail(req.email())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
            userOrAdmin = admin;
        }

        // Valida a senha
        if (!passwordEncoder.matches(req.password(), ((userOrAdmin instanceof User ? ((User) userOrAdmin).getPassword() : ((Admin) userOrAdmin).getPassword())))) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        // Gera o token JWT
        String token = jwtTokenProvider.generateToken(req.email(), userOrAdmin.getRole());
        return new AuthResponse(token, "Login bem-sucedido!", userOrAdmin.getRole());
    }

    /**
     * Cadastro de usuários (CUSTOMER ou ADMIN)
     * - Para ADMIN, envia OTP após verificar o email
     */
    public AuthResponse signup(SignupRequestDTO req) {
        if (req.role() == USER_ROLE.ROLE_ADMIN) {
            if (adminRepository.existsByEmail(req.email())) {
                throw new IllegalArgumentException("Admin já registrado com este email.");
            }

            // Gera e envia o OTP para o admin
            String encryptedPassword = passwordEncoder.encode(req.password());
            otpService.sendOtp(req.email(), req.email(), encryptedPassword);

            return new AuthResponse(null, "Admin registrado com sucesso. Valide o OTP.", "ROLE_ADMIN");
        } else {
            if (userRepository.existsByEmail(req.email())) {
                throw new IllegalArgumentException("Usuário já registrado com este email.");
            }

            User user = User.builder()
                    .email(req.email())
                    .password(passwordEncoder.encode(req.password()))
                    .name(req.email())
                    .build();
            userRepository.save(user);
            String token=jwtTokenProvider.generateToken(user.getEmail(), USER_ROLE.ROLE_CUSTOMER.name());

            return new AuthResponse(token, "Cliente registrado com sucesso.", "ROLE_CUSTOMER");
        }
    }







    /**
     * Validação do OTP para ADMIN concluir cadastro
     */
    public AuthResponse validateSignupOtp(OtpValidationRequestDTO req) {
        if (!otpService.validateOtp(req.email(), req.otp())) {
            throw new IllegalArgumentException("OTP inválido ou expirado.");
        }

        PendingAdmin pendingAdmin = otpService.getPendingAdmin(req.email());
        if (pendingAdmin == null) {
            throw new IllegalArgumentException("Nenhum cadastro pendente para este email.");
        }

        Admin admin = Admin.builder()
                .email(req.email())
                .password(pendingAdmin.getEncodedPassword())
                .name(pendingAdmin.getName())
                .build();
        adminRepository.save(admin);

        otpService.removePendingAdmin(req.email());

        String token = jwtTokenProvider.generateToken(admin.getEmail(), USER_ROLE.ROLE_ADMIN.name());

        return new AuthResponse(token, "Cadastro ADMIN concluído com sucesso!", "ROLE_ADMIN");
    }
}


