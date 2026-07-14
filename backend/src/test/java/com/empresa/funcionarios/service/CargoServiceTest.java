package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.CargoRequestDTO;
import com.empresa.funcionarios.dto.response.CargoResponseDTO;
import com.empresa.funcionarios.model.Cargo;
import com.empresa.funcionarios.repository.CargoRepository;
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
class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private CargoService cargoService;

    private Cargo cargo(Long id, String codigo, String descricao) {
        Cargo c = new Cargo();
        c.setCodigo(codigo);
        c.setDescricao(descricao);
        ReflectionTestUtils.setField(c, "id", id);
        return c;
    }

    @Test
    void salvarCadastraCargo() {
        when(cargoRepository.existsByCodigo("001")).thenReturn(false);
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> {
            Cargo salvo = inv.getArgument(0);
            ReflectionTestUtils.setField(salvo, "id", 1L);
            return salvo;
        });

        CargoResponseDTO response = cargoService.salvar(new CargoRequestDTO("001", "Analista"));

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCodigo()).isEqualTo("001");
        assertThat(response.getDescricao()).isEqualTo("Analista");
    }

    // regra principal do desafio: código de cargo duplicado não pode ser cadastrado
    @Test
    void salvarRejeitaCodigoDuplicado() {
        when(cargoRepository.existsByCodigo("001")).thenReturn(true);

        assertThatThrownBy(() -> cargoService.salvar(new CargoRequestDTO("001", "Analista")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe um cargo cadastrado com este código!");

        verify(cargoRepository, never()).save(any());
    }

    @Test
    void listarTodosMapeiaParaDTO() {
        when(cargoRepository.findAll()).thenReturn(List.of(
                cargo(1L, "001", "Analista"),
                cargo(2L, "002", "Gerente")));

        List<CargoResponseDTO> lista = cargoService.listarTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getCodigo()).isEqualTo("001");
        assertThat(lista.get(1).getDescricao()).isEqualTo("Gerente");
    }

    @Test
    void buscarPorIdRetornaCargoMapeado() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo(1L, "001", "Analista")));

        CargoResponseDTO response = cargoService.buscarPorId(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCodigo()).isEqualTo("001");
    }

    @Test
    void buscarPorIdInexistenteLancaErro() {
        when(cargoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cargoService.buscarPorId(99L))
                .hasMessage("Cargo não encontrado com o ID: 99");
    }

    // comportamento atual: o código enviado na edição é ignorado (imutável)
    @Test
    void editarAlteraApenasADescricaoMantendoOCodigo() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo(1L, "001", "Analista")));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        CargoResponseDTO response = cargoService.editar(1L, new CargoRequestDTO("999", "Analista Sênior"));

        assertThat(response.getCodigo()).isEqualTo("001");
        assertThat(response.getDescricao()).isEqualTo("Analista Sênior");
    }

    @Test
    void editarCargoInexistenteLancaErro() {
        when(cargoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cargoService.editar(99L, new CargoRequestDTO("001", "Analista")))
                .hasMessage("Cargo não encontrado para edição!");
    }

    @Test
    void deletarRemoveCargoExistente() {
        when(cargoRepository.existsById(1L)).thenReturn(true);

        cargoService.deletar(1L);

        verify(cargoRepository).deleteById(1L);
    }

    @Test
    void deletarCargoInexistenteLancaErro() {
        when(cargoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> cargoService.deletar(99L))
                .hasMessage("Cargo não encontrado para exclusão!");

        verify(cargoRepository, never()).deleteById(any());
    }
}
