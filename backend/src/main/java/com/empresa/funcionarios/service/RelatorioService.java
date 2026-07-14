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
        dados.put("totalCargos", cargoRepository.count());
        dados.put("totalDepartamentos", departamentoRepository.count());
        dados.put("totalVinculos", vinculoRepository.count());

        return dados;
    }

    public String gerarRelatorioFuncionariosCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("Nome;CPF;Empresa;Matricula;Cargo;Departamento\n");

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        for (Funcionario funcionario : funcionarios) {
            List<Vinculo> vinculos = vinculoRepository.findByFuncionarioId(funcionario.getId());

            if (vinculos.isEmpty()) {
                csv.append(linhaCsv(funcionario.getNome(), funcionario.getCpf(), "", "", "", ""));
            } else {
                for (Vinculo vinculo : vinculos) {
                    csv.append(linhaCsv(
                            funcionario.getNome(),
                            funcionario.getCpf(),
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

    private String linhaCsv(String nome, String cpf, String empresa, String matricula, String cargo, String departamento) {
        return String.join(";", nome, cpf, empresa, matricula, cargo, departamento) + "\n";
    }
}
