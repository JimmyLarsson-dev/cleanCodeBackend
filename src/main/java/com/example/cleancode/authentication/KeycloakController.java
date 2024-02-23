package com.example.cleancode.authentication;

import com.example.cleancode.authentication.dto.AssignRoleDTO;
import com.example.cleancode.authentication.dto.CreateUserDTO;
import com.example.cleancode.authentication.dto.TokenRequestObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/auth/keycloak")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KeycloakController {

    private final KeycloakService keycloakService;
    public KeycloakController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/getAdminToken")
    public String getAdminToken() throws Exception {
        return keycloakService.getAdminToken();
    }

    @PostMapping("/createCustomer")
    public String createCustomer (@RequestBody CreateUserDTO createUserDTO) {
        return keycloakService.createUser(createUserDTO);
    }

    //ADMIN endpoint. User token with Role ADMIN needed to access this endpoint
    @PostMapping("/createEmployee")
    public String createEmployee (@RequestBody CreateUserDTO createUserDTO) {
        return keycloakService.createUser(createUserDTO);
    }

    @PostMapping("/getUserId/{username}")
    public String getUserId(@PathVariable String username, @RequestHeader String adminToken) {

        return keycloakService.getUserId(username, adminToken);
    }

    @PostMapping("/assignRoleToUser")
    public String assignRoleToUser(@RequestBody AssignRoleDTO assignRoleDTO) {
        return keycloakService.assignRoleToUser(assignRoleDTO.getRole(), assignRoleDTO.getUsername(), assignRoleDTO.getAdminToken());
    }

    @PostMapping("/getUserToken")
    public ResponseEntity<TokenRequestObject> getUserToken(@RequestHeader String username, @RequestHeader String password) {
        return keycloakService.getUserToken(username, password);
    }
}
