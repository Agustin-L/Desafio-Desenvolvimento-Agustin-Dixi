package com.empresa.funcionarios.service;

import com.empresa.funcionarios.model.Cargo;
import com.empresa.funcionarios.model.Departamento;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.model.Vinculo;
import com.empresa.funcionarios.repository.CargoRepository;
import com.empresa.funcionarios.repository.DepartamentoRepository;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import com.empresa.funcionarios.repository.VinculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private VinculoRepository vinculoRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    private Funcionario funcionario(Long id, String nome, String cpf, boolean ativo) {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setCpf(cpf);
        f.setAtivo(ativo);
        ReflectionTestUtils.setField(f, "id", id);
        return f;
    }

    private Vinculo vinculo(String empresa, String matricula, String cargoCodigo, String deptoDescricao) {
        Cargo c = new Cargo();
        c.setCodigo(cargoCodigo);
        c.setDescricao("desc");
        Departamento d = new Departamento();
        d.setCodigo("X");
        d.setDescricao(deptoDescricao);
        Vinculo v = new Vinculo();
        v.setEmpresa(empresa);
        v.setMatricula(matricula);
        v.setCargo(c);
        v.setDepartamento(d);
        return v;
    }

    @Test
    void obterDadosGeraisRetornaAsContagens() {
        when(funcionarioRepository.count()).thenReturn(5L);
        when(funcionarioRepository.countByAtivoTrue()).thenReturn(4L);
        when(funcionarioRepository.countByAtivoFalse()).thenReturn(1L);
        when(cargoRepository.count()).thenReturn(3L);
        when(departamentoRepository.count()).thenReturn(2L);
        when(vinculoRepository.count()).thenReturn(7L);

        Map<String, Object> dados = relatorioService.obterDadosGerais();

        assertThat(dados)
                .containsEntry("totalFuncionarios", 5L)
                .containsEntry("totalFuncionariosAtivos", 4L)
                .containsEntry("totalFuncionariosInativos", 1L)
                .containsEntry("totalCargos", 3L)
                .containsEntry("totalDepartamentos", 2L)
                .containsEntry("totalVinculos", 7L);
    }

    @Test
    void csvSemFuncionariosContemApenasOCabecalho() {
        when(funcionarioRepository.findAll()).thenReturn(List.of());

        String csv = relatorioService.gerarRelatorioFuncionariosCsv();

        assertThat(csv).isEqualTo("Nome;CPF;Situacao;Empresa;Matricula;Cargo;Departamento\n");
    }

    // funcionário sem vínculo entra no relatório com os campos de vínculo vazios
    @Test
    void funcionarioSemVinculoGeraLinhaComCamposVazios() {
        when(funcionarioRepository.findAll())
                .thenReturn(List.of(funcionario(1L, "Ana", "529.982.247-25", true)));
        when(vinculoRepository.findByFuncionarioId(1L)).thenReturn(List.of());

        String csv = relatorioService.gerarRelatorioFuncionariosCsv();

        assertThat(csv).contains("Ana;529.982.247-25;Ativo;;;;\n");
    }

    // funcionário com N vínculos gera N linhas no relatório
    @Test
    void funcionarioComVariosVinculosGeraUmaLinhaPorVinculo() {
        when(funcionarioRepository.findAll())
                .thenReturn(List.of(funcionario(1L, "Ana", "529.982.247-25", true)));
        when(vinculoRepository.findByFuncionarioId(1L)).thenReturn(List.of(
                vinculo("Dixi Tecnologia", "1000", "001", "TI"),
                vinculo("Beta Corp", "2000", "002", "RH")));

        String csv = relatorioService.gerarRelatorioFuncionariosCsv();

        assertThat(csv).contains("Ana;529.982.247-25;Ativo;Dixi Tecnologia;1000;001;TI\n");
        assertThat(csv).contains("Ana;529.982.247-25;Ativo;Beta Corp;2000;002;RH\n");
        assertThat(csv.split("\n")).hasSize(3);
    }

    // funcionário inativo aparece com situação "Inativo"
    @Test
    void funcionarioInativoSaiComSituacaoInativo() {
        when(funcionarioRepository.findAll())
                .thenReturn(List.of(funcionario(2L, "Carla", "111.444.777-35", false)));
        when(vinculoRepository.findByFuncionarioId(2L)).thenReturn(List.of());

        String csv = relatorioService.gerarRelatorioFuncionariosCsv();

        assertThat(csv).contains("Carla;111.444.777-35;Inativo;;;;\n");
    }
}
