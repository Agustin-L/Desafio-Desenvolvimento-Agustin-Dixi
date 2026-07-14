package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.VinculoRequestDTO;
import com.empresa.funcionarios.dto.response.VinculoResponseDTO;
import com.empresa.funcionarios.model.Cargo;
import com.empresa.funcionarios.model.Departamento;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.model.Vinculo;
import com.empresa.funcionarios.repository.CargoRepository;
import com.empresa.funcionarios.repository.DepartamentoRepository;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import com.empresa.funcionarios.repository.VinculoRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VinculoServiceTest {

    @Mock
    private VinculoRepository vinculoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private VinculoService vinculoService;

    private Funcionario ana;
    private Cargo cargoAnalista;
    private Departamento deptoTi;

    @BeforeEach
    void setUp() {
        ana = new Funcionario();
        ana.setNome("Ana");
        ana.setCpf("529.982.247-25");
        ReflectionTestUtils.setField(ana, "id", 1L);

        cargoAnalista = new Cargo();
        cargoAnalista.setCodigo("001");
        cargoAnalista.setDescricao("Analista");
        ReflectionTestUtils.setField(cargoAnalista, "id", 2L);

        deptoTi = new Departamento();
        deptoTi.setCodigo("D1");
        deptoTi.setDescricao("TI");
        ReflectionTestUtils.setField(deptoTi, "id", 3L);
    }

    private Vinculo vinculo(Long id, String empresa, String matricula) {
        Vinculo v = new Vinculo();
        v.setEmpresa(empresa);
        v.setMatricula(matricula);
        v.setFuncionario(ana);
        v.setCargo(cargoAnalista);
        v.setDepartamento(deptoTi);
        ReflectionTestUtils.setField(v, "id", id);
        return v;
    }

    // criação de vínculo resolve as 3 chaves estrangeiras e mapeia o DTO completo
    @Test
    void salvarCriaVinculoResolvendoFuncionarioCargoEDepartamento() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(cargoRepository.findById(2L)).thenReturn(Optional.of(cargoAnalista));
        when(departamentoRepository.findById(3L)).thenReturn(Optional.of(deptoTi));
        when(vinculoRepository.save(any(Vinculo.class))).thenAnswer(inv -> {
            Vinculo v = inv.getArgument(0);
            ReflectionTestUtils.setField(v, "id", 10L);
            return v;
        });

        VinculoResponseDTO response =
                vinculoService.salvar(new VinculoRequestDTO("Dixi Tecnologia", "1000", 1L, 2L, 3L));

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmpresa()).isEqualTo("Dixi Tecnologia");
        assertThat(response.getMatricula()).isEqualTo("1000");
        assertThat(response.getFuncionarioId()).isEqualTo(1L);
        assertThat(response.getFuncionarioNome()).isEqualTo("Ana");
        assertThat(response.getCargoId()).isEqualTo(2L);
        assertThat(response.getCargoCodigo()).isEqualTo("001");
        assertThat(response.getDepartamentoId()).isEqualTo(3L);
        assertThat(response.getDepartamentoNome()).isEqualTo("TI");
    }

    @Test
    void salvarComFuncionarioInexistenteLancaErro() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.salvar(new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Funcionário informado não existe!");

        verify(vinculoRepository, never()).save(any());
    }

    // regra nova: funcionário inativo não pode receber vínculo
    @Test
    void salvarParaFuncionarioInativoLancaErro() {
        ana.setAtivo(false);
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(ana));

        assertThatThrownBy(() ->
                vinculoService.salvar(new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Não é possível criar vínculo para funcionário inativo!");

        verify(vinculoRepository, never()).save(any());
    }

    @Test
    void salvarComCargoInexistenteLancaErro() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(cargoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.salvar(new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Cargo informado não existe!");

        verify(vinculoRepository, never()).save(any());
    }

    @Test
    void salvarComDepartamentoInexistenteLancaErro() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(cargoRepository.findById(2L)).thenReturn(Optional.of(cargoAnalista));
        when(departamentoRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.salvar(new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Departamento informado não existe!");

        verify(vinculoRepository, never()).save(any());
    }

    // edição atualiza empresa, matrícula, cargo e departamento, mas nunca troca o funcionário
    @Test
    void editarAtualizaDadosSemTrocarOFuncionario() {
        Cargo cargoGerente = new Cargo();
        cargoGerente.setCodigo("002");
        cargoGerente.setDescricao("Gerente");
        ReflectionTestUtils.setField(cargoGerente, "id", 4L);

        when(vinculoRepository.findById(10L)).thenReturn(Optional.of(vinculo(10L, "Dixi", "1000")));
        when(cargoRepository.findById(4L)).thenReturn(Optional.of(cargoGerente));
        when(departamentoRepository.findById(3L)).thenReturn(Optional.of(deptoTi));
        when(vinculoRepository.save(any(Vinculo.class))).thenAnswer(inv -> inv.getArgument(0));

        VinculoResponseDTO response =
                vinculoService.editar(10L, new VinculoRequestDTO("Beta Corp", "2000", 99L, 4L, 3L));

        assertThat(response.getEmpresa()).isEqualTo("Beta Corp");
        assertThat(response.getMatricula()).isEqualTo("2000");
        assertThat(response.getCargoCodigo()).isEqualTo("002");
        assertThat(response.getFuncionarioNome()).isEqualTo("Ana");
        verifyNoInteractions(funcionarioRepository);
    }

    @Test
    void editarVinculoInexistenteLancaErro() {
        when(vinculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.editar(99L, new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Vínculo não encontrado para edição!");
    }

    @Test
    void editarComCargoInexistenteLancaErro() {
        when(vinculoRepository.findById(10L)).thenReturn(Optional.of(vinculo(10L, "Dixi", "1000")));
        when(cargoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.editar(10L, new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Cargo informado não existe!");

        verify(vinculoRepository, never()).save(any());
    }

    @Test
    void editarComDepartamentoInexistenteLancaErro() {
        when(vinculoRepository.findById(10L)).thenReturn(Optional.of(vinculo(10L, "Dixi", "1000")));
        when(cargoRepository.findById(2L)).thenReturn(Optional.of(cargoAnalista));
        when(departamentoRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                vinculoService.editar(10L, new VinculoRequestDTO("Dixi", "1000", 1L, 2L, 3L)))
                .hasMessage("Departamento informado não existe!");

        verify(vinculoRepository, never()).save(any());
    }

    @Test
    void listarPorFuncionarioMapeiaOsVinculos() {
        when(vinculoRepository.findByFuncionarioId(1L))
                .thenReturn(List.of(vinculo(10L, "Dixi", "1000"), vinculo(11L, "Beta", "2000")));

        List<VinculoResponseDTO> lista = vinculoService.listarPorFuncionario(1L);

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getEmpresa()).isEqualTo("Dixi");
        assertThat(lista.get(1).getEmpresa()).isEqualTo("Beta");
    }

    @Test
    void listarTodosMapeiaOsVinculos() {
        when(vinculoRepository.findAll()).thenReturn(List.of(vinculo(10L, "Dixi", "1000")));

        List<VinculoResponseDTO> lista = vinculoService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getFuncionarioNome()).isEqualTo("Ana");
    }

    @Test
    void buscarPorIdRetornaVinculoMapeado() {
        when(vinculoRepository.findById(10L)).thenReturn(Optional.of(vinculo(10L, "Dixi", "1000")));

        VinculoResponseDTO response = vinculoService.buscarPorId(10L);

        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void buscarPorIdInexistenteLancaErro() {
        when(vinculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vinculoService.buscarPorId(99L))
                .hasMessage("Vínculo não encontrado com o ID: 99");
    }

    @Test
    void deletarRemoveVinculoExistente() {
        when(vinculoRepository.existsById(10L)).thenReturn(true);

        vinculoService.deletar(10L);

        verify(vinculoRepository).deleteById(10L);
    }

    @Test
    void deletarVinculoInexistenteLancaErro() {
        when(vinculoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vinculoService.deletar(99L))
                .hasMessage("Vínculo não encontrado para exclusão!");

        verify(vinculoRepository, never()).deleteById(any());
    }
}
