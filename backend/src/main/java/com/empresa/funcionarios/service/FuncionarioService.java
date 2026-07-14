package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.FuncionarioRequestDTO;
import com.empresa.funcionarios.dto.response.FuncionarioResponseDTO;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<FuncionarioResponseDTO> listarComFiltros(String nome, String cpf, String matricula, String empresa, Long cargoId, Long departamentoId) {
        return funcionarioRepository.buscarComFiltros(
                        vazioParaNulo(nome), vazioParaNulo(cpf), vazioParaNulo(matricula), vazioParaNulo(empresa), cargoId, departamentoId)
                .stream()
                .map(func -> new FuncionarioResponseDTO(func.getId(), func.getNome(), func.getCpf()))
                .toList();
    }

    private String vazioParaNulo(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor;
    }

    public FuncionarioResponseDTO salvar(FuncionarioRequestDTO request) {
        if (funcionarioRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("Já existe um funcionário cadastrado com este CPF!");
        }

        Funcionario funcionarioModel = new Funcionario();
        funcionarioModel.setNome(request.getNome().trim());
        funcionarioModel.setCpf(request.getCpf());

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioModel);

        return new FuncionarioResponseDTO(
                funcionarioSalvo.getId(),
                funcionarioSalvo.getNome(),
                funcionarioSalvo.getCpf()
        );
    }

    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));

        return new FuncionarioResponseDTO(
                func.getId(),
                func.getNome(),
                func.getCpf()
        );
    }

    public FuncionarioResponseDTO editar(Long id, FuncionarioRequestDTO request) {
        Funcionario funcExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado para edição!"));

        boolean cpfAlterado = !funcExistente.getCpf().equals(request.getCpf());
        if (cpfAlterado && funcionarioRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("Já existe um funcionário cadastrado com este CPF!");
        }

        funcExistente.setNome(request.getNome().trim());
        funcExistente.setCpf(request.getCpf());

        Funcionario funcAtualizado = funcionarioRepository.save(funcExistente);

        return new FuncionarioResponseDTO(
                funcAtualizado.getId(),
                funcAtualizado.getNome(),
                funcAtualizado.getCpf()
        );
    }

    public void deletar(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado para exclusão!");
        }
        funcionarioRepository.deleteById(id);
    }
}
