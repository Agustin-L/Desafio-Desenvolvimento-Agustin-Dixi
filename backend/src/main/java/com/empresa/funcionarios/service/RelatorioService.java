package com.empresa.funcionarios.service;

import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.model.Vinculo;
import com.empresa.funcionarios.repository.CargoRepository;
import com.empresa.funcionarios.repository.DepartamentoRepository;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import com.empresa.funcionarios.repository.VinculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private VinculoRepository vinculoRepository;

    public Map<String, Object> obterDadosGerais() {
        Map<String, Object> dados = new HashMap<>();

        dados.put("totalFuncionarios", funcionarioRepository.count());
        dados.put("totalFuncionariosAtivos", funcionarioRepository.countByAtivoTrue());
        dados.put("totalFuncionariosInativos", funcionarioRepository.countByAtivoFalse());
        dados.put("totalCargos", cargoRepository.count());
        dados.put("totalDepartamentos", departamentoRepository.count());
        dados.put("totalVinculos", vinculoRepository.count());

        return dados;
    }

    public String gerarRelatorioFuncionariosCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("Nome;CPF;Situacao;Empresa;Matricula;Cargo;Departamento\n");

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        for (Funcionario funcionario : funcionarios) {
            List<Vinculo> vinculos = vinculoRepository.findByFuncionarioId(funcionario.getId());
            String situacao = Boolean.FALSE.equals(funcionario.getAtivo()) ? "Inativo" : "Ativo";

            if (vinculos.isEmpty()) {
                csv.append(linhaCsv(funcionario.getNome(), formatarCpf(funcionario.getCpf()), situacao, "", "", "", ""));
            } else {
                for (Vinculo vinculo : vinculos) {
                    csv.append(linhaCsv(
                            funcionario.getNome(),
                            formatarCpf(funcionario.getCpf()),
                            situacao,
                            vinculo.getEmpresa(),
                            vinculo.getMatricula(),
                            vinculo.getCargo().getCodigo(),
                            vinculo.getDepartamento().getDescricao()
                    ));
                }
            }
        }

        return csv.toString();
    }

    private String linhaCsv(String nome, String cpf, String situacao, String empresa, String matricula, String cargo, String departamento) {
        return String.join(";", nome, cpf, situacao, empresa, matricula, cargo, departamento) + "\n";
    }

    // CPF é armazenado só com dígitos; máscara aplicada apenas para exibição
    private String formatarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return cpf == null ? "" : cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}
