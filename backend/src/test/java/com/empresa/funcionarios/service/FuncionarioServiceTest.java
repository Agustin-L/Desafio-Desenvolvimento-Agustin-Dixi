package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.FuncionarioRequestDTO;
import com.empresa.funcionarios.dto.response.FuncionarioResponseDTO;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    // cria entidade com id simulado, já que o model não expõe setId (ativo=true por padrão)
    private Funcionario funcionario(Long id, String nome, String cpf) {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setCpf(cpf);
        ReflectionTestUtils.setField(f, "id", id);
        return f;
    }

    @Test
    void salvarCadastraFuncionarioComNomeSemEspacosNasBordas() {
        when(funcionarioRepository.existsByCpf("529.982.247-25")).thenReturn(false);
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> {
            Funcionario salvo = inv.getArgument(0);
            ReflectionTestUtils.setField(salvo, "id", 1L);
            return salvo;
        });

        FuncionarioResponseDTO response =
                funcionarioService.salvar(new FuncionarioRequestDTO("  Maria Silva  ", "529.982.247-25"));

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("Maria Silva");
        assertThat(response.getCpf()).isEqualTo("529.982.247-25");
        assertThat(response.getAtivo()).isTrue();
    }

    // regra principal do desafio: CPF duplicado não pode ser cadastrado
    @Test
    void salvarRejeitaCpfDuplicado() {
        when(funcionarioRepository.existsByCpf("529.982.247-25")).thenReturn(true);

        assertThatThrownBy(() ->
                funcionarioService.salvar(new FuncionarioRequestDTO("Maria", "529.982.247-25")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe um funcionário cadastrado com este CPF!");

        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    void buscarPorIdRetornaFuncionarioMapeado() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));

        FuncionarioResponseDTO response = funcionarioService.buscarPorId(7L);

        assertThat(response.getId()).isEqualTo(7L);
        assertThat(response.getNome()).isEqualTo("Ana");
        assertThat(response.getCpf()).isEqualTo("123.456.789-09");
    }

    @Test
    void buscarPorIdInexistenteLancaErro() {
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.buscarPorId(99L))
                .hasMessage("Funcionário não encontrado com o ID: 99");
    }

    // manter o mesmo CPF na edição não dispara a checagem de duplicidade
    @Test
    void editarComCpfInalteradoNaoChecaDuplicidade() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponseDTO response = funcionarioService.editar(7L,
                new FuncionarioRequestDTO("Ana Paula", "123.456.789-09"));

        assertThat(response.getNome()).isEqualTo("Ana Paula");
        verify(funcionarioRepository, never()).existsByCpf(any());
    }

    @Test
    void editarComCpfAlteradoParaUmJaExistenteLancaErro() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));
        when(funcionarioRepository.existsByCpf("529.982.247-25")).thenReturn(true);

        assertThatThrownBy(() -> funcionarioService.editar(7L,
                new FuncionarioRequestDTO("Ana", "529.982.247-25")))
                .hasMessage("Já existe um funcionário cadastrado com este CPF!");

        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    void editarComCpfAlteradoParaUmLivreSalva() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));
        when(funcionarioRepository.existsByCpf("529.982.247-25")).thenReturn(false);
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponseDTO response = funcionarioService.editar(7L,
                new FuncionarioRequestDTO("Ana", "529.982.247-25"));

        assertThat(response.getCpf()).isEqualTo("529.982.247-25");
    }

    @Test
    void editarFuncionarioInexistenteLancaErro() {
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.editar(99L,
                new FuncionarioRequestDTO("Ana", "123.456.789-09")))
                .hasMessage("Funcionário não encontrado para edição!");
    }

    // regra CLT: funcionário não é excluído, apenas inativado
    @Test
    void inativarMarcaFuncionarioComoInativo() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponseDTO response = funcionarioService.inativar(7L);

        assertThat(response.getAtivo()).isFalse();
    }

    @Test
    void inativarFuncionarioJaInativoLancaErro() {
        Funcionario inativo = funcionario(7L, "Ana", "123.456.789-09");
        inativo.setAtivo(false);
        when(funcionarioRepository.findById(7L)).thenReturn(Optional.of(inativo));

        assertThatThrownBy(() -> funcionarioService.inativar(7L))
                .hasMessage("Funcionário já está inativo!");

        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    void inativarFuncionarioInexistenteLancaErro() {
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.inativar(99L))
                .hasMessage("Funcionário não encontrado para inativação!");
    }

    @Test
    void reativarMarcaFuncionarioComoAtivo() {
        Funcionario inativo = funcionario(7L, "Ana", "123.456.789-09");
        inativo.setAtivo(false);
        when(funcionarioRepository.findById(7L)).thenReturn(Optional.of(inativo));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponseDTO response = funcionarioService.reativar(7L);

        assertThat(response.getAtivo()).isTrue();
    }

    @Test
    void reativarFuncionarioJaAtivoLancaErro() {
        when(funcionarioRepository.findById(7L))
                .thenReturn(Optional.of(funcionario(7L, "Ana", "123.456.789-09")));

        assertThatThrownBy(() -> funcionarioService.reativar(7L))
                .hasMessage("Funcionário já está ativo!");

        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    void reativarFuncionarioInexistenteLancaErro() {
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.reativar(99L))
                .hasMessage("Funcionário não encontrado para reativação!");
    }

    // filtros em branco viram null antes de chegar na query
    @Test
    void listarComFiltrosConverteVazioEEmBrancoParaNulo() {
        when(funcionarioRepository.buscarComFiltros(null, null, null, null, null, null, null))
                .thenReturn(List.of());

        funcionarioService.listarComFiltros("", "   ", null, "", null, null, null);

        verify(funcionarioRepository).buscarComFiltros(null, null, null, null, null, null, null);
    }

    @Test
    void listarComFiltrosRepassaValoresPreenchidosEMapeia() {
        when(funcionarioRepository.buscarComFiltros("Ana", "123", "1000", "Dixi", 2L, 3L, true))
                .thenReturn(List.of(funcionario(1L, "Ana", "123.456.789-09")));

        List<FuncionarioResponseDTO> lista =
                funcionarioService.listarComFiltros("Ana", "123", "1000", "Dixi", 2L, 3L, true);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getId()).isEqualTo(1L);
        assertThat(lista.get(0).getNome()).isEqualTo("Ana");
    }
}
