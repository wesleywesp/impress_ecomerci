package impress.weasp.controller.dto;


import impress.weasp.controller.dto.perfil.admin.AdminProfileUpdateDTO;
import impress.weasp.model.Admin;
import impress.weasp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/profile")
    public ResponseEntity<Admin> getProfile(Principal principal) {
        Admin admin = adminService.getAdminProfile(principal.getName());
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/profile")
    public ResponseEntity<Admin> updateProfile(@RequestBody AdminProfileUpdateDTO dto, Principal principal) {
        Admin updatedAdmin = adminService.updateAdminProfile(dto, principal.getName());
        return ResponseEntity.ok(updatedAdmin);
    }
}
