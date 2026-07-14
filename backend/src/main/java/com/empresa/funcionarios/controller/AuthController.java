package com.empresa.funcionarios.controller;

import com.empresa.funcionarios.dto.request.LoginRequestDTO;
import com.empresa.funcionarios.dto.response.LoginResponseDTO;
import com.empresa.funcionarios.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // credenciais inválidas propagam AuthenticationException -> 401 no TratadorDeExcecoes
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.gerarToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token, userDetails.getUsername(), extrairPerfil(userDetails)));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        // rota coberta pelo permitAll de /api/auth/** — sem token o Authentication chega nulo
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Não autenticado. Faça login novamente."));
        }
        return ResponseEntity.ok(new LoginResponseDTO(null, userDetails.getUsername(), extrairPerfil(userDetails)));
    }

    private String extrairPerfil(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_PADRAO")
                .replace("ROLE_", "");
    }
}
