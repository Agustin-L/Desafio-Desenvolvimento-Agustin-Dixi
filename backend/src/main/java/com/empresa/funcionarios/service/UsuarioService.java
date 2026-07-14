package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.UsuarioRequestDTO;
import com.empresa.funcionarios.dto.response.UsuarioResponseDTO;
import com.empresa.funcionarios.exception.RecursoNaoEncontradoException;
import com.empresa.funcionarios.exception.RegraDeNegocioException;
import com.empresa.funcionarios.model.Perfil;
import com.empresa.funcionarios.model.Usuario;
import com.empresa.funcionarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getUsername(), u.getPerfil().name()))
                .toList();
    }

    public UsuarioResponseDTO salvar(UsuarioRequestDTO request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RegraDeNegocioException("Informe o nome de usuário!");
        }
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com este nome de acesso!");
        }
        if (request.getSenha() == null || request.getSenha().length() < 6) {
            throw new RegraDeNegocioException("A senha deve ter no mínimo 6 caracteres!");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setPerfil(request.getPerfil() == Perfil.ADMIN ? Perfil.ADMIN : Perfil.PADRAO);

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(salvo.getId(), salvo.getUsername(), salvo.getPerfil().name());
    }

    public void deletar(Long id, String usernameLogado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para exclusão!"));

        if (usuario.getUsername().equals(usernameLogado)) {
            throw new RegraDeNegocioException("Você não pode excluir o seu próprio acesso!");
        }

        usuarioRepository.deleteById(id);
    }
}
