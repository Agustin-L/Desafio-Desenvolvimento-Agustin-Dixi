package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.FuncionarioRequestDTO;
import com.empresa.funcionarios.dto.response.FuncionarioResponseDTO;
import com.empresa.funcionarios.exception.RecursoNaoEncontradoException;
import com.empresa.funcionarios.exception.RegraDeNegocioException;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<FuncionarioResponseDTO> listarComFiltros(String nome, String cpf, String matricula, String empresa, Long cargoId, Long departamentoId, Boolean ativo) {
        return funcionarioRepository.buscarComFiltros(
                        vazioParaNulo(nome), vazioParaNulo(normalizarCpf(cpf)), vazioParaNulo(matricula), vazioParaNulo(empresa), cargoId, departamentoId, ativo)
                .stream()
                .map(this::paraResponseDTO)
                .toList();
    }

    private String vazioParaNulo(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor;
    }

    // CPF é persistido e comparado sempre sem máscara (somente dígitos)
    private String normalizarCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    public FuncionarioResponseDTO salvar(FuncionarioRequestDTO request) {
        String cpf = normalizarCpf(request.getCpf());
        if (funcionarioRepository.existsByCpf(cpf)) {
            throw new RegraDeNegocioException("Já existe um funcionário cadastrado com este CPF!");
        }

        Funcionario funcionarioModel = new Funcionario();
        funcionarioModel.setNome(request.getNome().trim());
        funcionarioModel.setCpf(cpf);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioModel);

        return paraResponseDTO(funcionarioSalvo);
    }

    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado com o ID: " + id));

        return paraResponseDTO(func);
    }

    public FuncionarioResponseDTO editar(Long id, FuncionarioRequestDTO request) {
        Funcionario funcExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado para edição!"));

        String cpf = normalizarCpf(request.getCpf());
        boolean cpfAlterado = !funcExistente.getCpf().equals(cpf);
        if (cpfAlterado && funcionarioRepository.existsByCpf(cpf)) {
            throw new RegraDeNegocioException("Já existe um funcionário cadastrado com este CPF!");
        }

        funcExistente.setNome(request.getNome().trim());
        funcExistente.setCpf(cpf);

        Funcionario funcAtualizado = funcionarioRepository.save(funcExistente);

        return paraResponseDTO(funcAtualizado);
    }

    // Regra CLT: funcionário nunca é excluído do sistema, apenas inativado
    public FuncionarioResponseDTO inativar(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado para inativação!"));

        if (Boolean.FALSE.equals(func.getAtivo())) {
            throw new RegraDeNegocioException("Funcionário já está inativo!");
        }

        func.setAtivo(false);
        return paraResponseDTO(funcionarioRepository.save(func));
    }

    public FuncionarioResponseDTO reativar(Long id) {
        Funcionario func = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado para reativação!"));

        if (Boolean.TRUE.equals(func.getAtivo())) {
            throw new RegraDeNegocioException("Funcionário já está ativo!");
        }

        func.setAtivo(true);
        return paraResponseDTO(funcionarioRepository.save(func));
    }

    private FuncionarioResponseDTO paraResponseDTO(Funcionario func) {
        return new FuncionarioResponseDTO(
                func.getId(),
                func.getNome(),
                func.getCpf(),
                func.getAtivo()
        );
    }
}
