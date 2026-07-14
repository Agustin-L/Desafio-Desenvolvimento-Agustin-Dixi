package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.DepartamentoRequestDTO;
import com.empresa.funcionarios.dto.response.DepartamentoResponseDTO;
import com.empresa.funcionarios.model.Departamento;
import com.empresa.funcionarios.repository.DepartamentoRepository;
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
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoService departamentoService;

    private Departamento departamento(Long id, String codigo, String descricao) {
        Departamento d = new Departamento();
        d.setCodigo(codigo);
        d.setDescricao(descricao);
        ReflectionTestUtils.setField(d, "id", id);
        return d;
    }

    @Test
    void salvarCadastraDepartamento() {
        when(departamentoRepository.existsByCodigo("D1")).thenReturn(false);
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(inv -> {
            Departamento salvo = inv.getArgument(0);
            ReflectionTestUtils.setField(salvo, "id", 1L);
            return salvo;
        });

        DepartamentoResponseDTO response =
                departamentoService.salvar(new DepartamentoRequestDTO("D1", "TI"));

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCodigo()).isEqualTo("D1");
        assertThat(response.getDescricao()).isEqualTo("TI");
    }

    // regra principal do desafio: código de departamento duplicado não pode ser cadastrado
    @Test
    void salvarRejeitaCodigoDuplicado() {
        when(departamentoRepository.existsByCodigo("D1")).thenReturn(true);

        assertThatThrownBy(() -> departamentoService.salvar(new DepartamentoRequestDTO("D1", "TI")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe um departamento cadastrado com este código!");

        verify(departamentoRepository, never()).save(any());
    }

    @Test
    void listarTodosMapeiaParaDTO() {
        when(departamentoRepository.findAll()).thenReturn(List.of(
                departamento(1L, "D1", "TI"),
                departamento(2L, "D2", "RH")));

        List<DepartamentoResponseDTO> lista = departamentoService.listarTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(1).getCodigo()).isEqualTo("D2");
    }

    @Test
    void buscarPorIdRetornaDepartamentoMapeado() {
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento(1L, "D1", "TI")));

        DepartamentoResponseDTO response = departamentoService.buscarPorId(1L);

        assertThat(response.getCodigo()).isEqualTo("D1");
    }

    @Test
    void buscarPorIdInexistenteLancaErro() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departamentoService.buscarPorId(99L))
                .hasMessage("Departamento não encontrado com o ID: 99");
    }

    // comportamento atual (divergente do Cargo): a edição troca o código e NÃO valida duplicidade
    @Test
    void editarTrocaCodigoEDescricaoSemChecarDuplicidade() {
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento(1L, "D1", "TI")));
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(inv -> inv.getArgument(0));

        DepartamentoResponseDTO response =
                departamentoService.editar(1L, new DepartamentoRequestDTO("D9", "Tecnologia"));

        assertThat(response.getCodigo()).isEqualTo("D9");
        assertThat(response.getDescricao()).isEqualTo("Tecnologia");
        verify(departamentoRepository, never()).existsByCodigo(any());
    }

    @Test
    void editarDepartamentoInexistenteLancaErro() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departamentoService.editar(99L, new DepartamentoRequestDTO("D1", "TI")))
                .hasMessage("Departamento não encontrado para edição!");
    }

    @Test
    void deletarRemoveDepartamentoExistente() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);

        departamentoService.deletar(1L);

        verify(departamentoRepository).deleteById(1L);
    }

    @Test
    void deletarDepartamentoInexistenteLancaErro() {
        when(departamentoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> departamentoService.deletar(99L))
                .hasMessage("Departamento não encontrado para exclusão!");

        verify(departamentoRepository, never()).deleteById(any());
    }
}
