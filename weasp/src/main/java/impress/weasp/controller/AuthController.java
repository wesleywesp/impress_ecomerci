package impress.weasp.controller;

import impress.weasp.controller.dto.auth.*;
import impress.weasp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Login para usuários (CUSTOMER ou ADMIN)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDTO req) {
        System.out.println("Requisição recebida: " + req);
        AuthResponse response = authService.login(req);
        System.out.println("Resposta gerada: " + response);
        return ResponseEntity.ok(response);
    }


    /**
     * Signup para novos usuários (CUSTOMER ou ADMIN)
     * - Para ADMIN, envia OTP após verificar o email
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequestDTO req) {
        AuthResponse response = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Validação do OTP para ADMIN concluir cadastro
     */
    @PostMapping("/validate-signup-otp")
    public ResponseEntity<AuthResponse> validateSignupOtp(@RequestBody OtpValidationRequestDTO req) {
       AuthResponse response =authService.validateSignupOtp(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


