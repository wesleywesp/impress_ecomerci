package impress.weasp.service;

import impress.weasp.controller.dto.perfil.admin.AdminProfileUpdateDTO;
import impress.weasp.model.Admin;
import impress.weasp.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin updateAdminProfile(AdminProfileUpdateDTO dto, String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado."));

        if (dto.name() != null) admin.setName(dto.name());
        if (dto.password() != null) admin.setPassword(passwordEncoder.encode(dto.password()));

        return adminRepository.save(admin);
    }

    public Admin getAdminProfile(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado."));
    }

}
