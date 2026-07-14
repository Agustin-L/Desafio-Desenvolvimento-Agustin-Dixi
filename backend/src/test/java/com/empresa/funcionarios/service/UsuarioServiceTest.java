package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.UsuarioRequestDTO;
import com.empresa.funcionarios.dto.response.UsuarioResponseDTO;
import com.empresa.funcionarios.model.Perfil;
import com.empresa.funcionarios.model.Usuario;
import com.empresa.funcionarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario(Long id, String username, Perfil perfil) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setUsername(username);
        u.setSenha("hash");
        u.setPerfil(perfil);
        return u;
    }

    // a senha nunca é salva em texto puro
    @Test
    void salvarCadastraUsuarioComSenhaCodificada() {
        when(usuarioRepository.existsByUsername("joao")).thenReturn(false);
        when(passwordEncoder.encode("segredo1")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO response =
                usuarioService.salvar(new UsuarioRequestDTO("joao", "segredo1", Perfil.ADMIN));

        assertThat(response.getUsername()).isEqualTo("joao");
        assertThat(response.getPerfil()).isEqualTo("ADMIN");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("hash-bcrypt");
    }

    @Test
    void salvarSemUsernameLancaErro() {
        assertThatThrownBy(() -> usuarioService.salvar(new UsuarioRequestDTO(null, "segredo1", Perfil.PADRAO)))
                .hasMessage("Informe o nome de usuário!");
        assertThatThrownBy(() -> usuarioService.salvar(new UsuarioRequestDTO("   ", "segredo1", Perfil.PADRAO)))
                .hasMessage("Informe o nome de usuário!");
    }

    @Test
    void salvarComUsernameDuplicadoLancaErro() {
        when(usuarioRepository.existsByUsername("joao")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.salvar(new UsuarioRequestDTO("joao", "segredo1", Perfil.PADRAO)))
                .hasMessage("Já existe um usuário cadastrado com este nome de acesso!");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void salvarComSenhaCurtaOuNulaLancaErro() {
        when(usuarioRepository.existsByUsername("joao")).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.salvar(new UsuarioRequestDTO("joao", "12345", Perfil.PADRAO)))
                .hasMessage("A senha deve ter no mínimo 6 caracteres!");
        assertThatThrownBy(() -> usuarioService.salvar(new UsuarioRequestDTO("joao", null, Perfil.PADRAO)))
                .hasMessage("A senha deve ter no mínimo 6 caracteres!");
    }

    // qualquer perfil que não seja ADMIN vira PADRAO
    @Test
    void salvarComPerfilNuloCadastraComoPadrao() {
        when(usuarioRepository.existsByUsername("joao")).thenReturn(false);
        when(passwordEncoder.encode("segredo1")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO response =
                usuarioService.salvar(new UsuarioRequestDTO("joao", "segredo1", null));

        assertThat(response.getPerfil()).isEqualTo("PADRAO");
    }

    @Test
    void listarTodosMapeiaParaDTO() {
        when(usuarioRepository.findAll()).thenReturn(List.of(
                usuario(1L, "admin", Perfil.ADMIN),
                usuario(2L, "maria", Perfil.PADRAO)));

        List<UsuarioResponseDTO> lista = usuarioService.listarTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getPerfil()).isEqualTo("ADMIN");
        assertThat(lista.get(1).getUsername()).isEqualTo("maria");
    }

    // regra do sistema: usuário não pode excluir o próprio acesso
    @Test
    void deletarOProprioAcessoLancaErro() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario(5L, "admin", Perfil.ADMIN)));

        assertThatThrownBy(() -> usuarioService.deletar(5L, "admin"))
                .hasMessage("Você não pode excluir o seu próprio acesso!");

        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void deletarOutroUsuarioRemove() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario(5L, "maria", Perfil.PADRAO)));

        usuarioService.deletar(5L, "admin");

        verify(usuarioRepository).deleteById(5L);
    }

    @Test
    void deletarUsuarioInexistenteLancaErro() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.deletar(99L, "admin"))
                .hasMessage("Usuário não encontrado para exclusão!");
    }
}
