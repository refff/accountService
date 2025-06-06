package account.presentation;

import account.domain.RolesChanger;
import account.domain.StatusChanger;
import account.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "api/admin/user/")
    public ResponseEntity<?> getUsersList() {
        return adminService.getUsersList();
    }

    @DeleteMapping(value = "api/admin/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "email", required = false) String email) {
        return adminService.deleteUser(email);
    }

    @PutMapping(value = "api/admin/user/role")
    public ResponseEntity<?> changeRoles(@RequestBody RolesChanger changer) {
        return adminService.changeRole(changer);
    }

    @PutMapping(value = "api/admin/user/access")
    public ResponseEntity<?> changeStatus(@RequestBody StatusChanger changer) {
        return adminService.changeStatus(changer);
    }

}
