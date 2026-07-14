package com.empresa.funcionarios.config;

import com.empresa.funcionarios.model.Perfil;
import com.empresa.funcionarios.model.Usuario;
import com.empresa.funcionarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final String USUARIO_ADMIN_PADRAO = "admin";
    private static final String SENHA_ADMIN_PADRAO = "admin123";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername(USUARIO_ADMIN_PADRAO);
            admin.setSenha(passwordEncoder.encode(SENHA_ADMIN_PADRAO));
            admin.setPerfil(Perfil.ADMIN);
            usuarioRepository.save(admin);

            log.warn("==============================================================");
            log.warn(" Usuário administrador padrão criado.");
            log.warn(" Usuário: {}", USUARIO_ADMIN_PADRAO);
            log.warn(" Senha:   {}", SENHA_ADMIN_PADRAO);
            log.warn(" Troque essa senha assim que possível (crie outro usuário admin");
            log.warn(" e remova este, ou implemente troca de senha).");
            log.warn("==============================================================");
        }
    }
}
