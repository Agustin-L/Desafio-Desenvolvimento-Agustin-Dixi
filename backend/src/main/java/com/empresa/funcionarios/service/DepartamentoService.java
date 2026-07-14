package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.DepartamentoRequestDTO;
import com.empresa.funcionarios.dto.response.DepartamentoResponseDTO;
import com.empresa.funcionarios.model.Departamento;
import com.empresa.funcionarios.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<DepartamentoResponseDTO> listarTodos() {
        return departamentoRepository.findAll().stream()
                .map(depto -> new DepartamentoResponseDTO(depto.getId(), depto.getCodigo(), depto.getDescricao()))
                .toList();
    }

    public DepartamentoResponseDTO salvar(DepartamentoRequestDTO request) {
        if (departamentoRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Já existe um departamento cadastrado com este código!");
        }

        Departamento deptoModel = new Departamento();
        deptoModel.setCodigo(request.getCodigo());
        deptoModel.setDescricao(request.getDescricao());

        Departamento deptoSalvo = departamentoRepository.save(deptoModel);

        return new DepartamentoResponseDTO(deptoSalvo.getId(), deptoSalvo.getCodigo(), deptoSalvo.getDescricao());
    }

    public DepartamentoResponseDTO buscarPorId(Long id) {
        Departamento depto = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com o ID: " + id));

        return new DepartamentoResponseDTO(depto.getId(), depto.getCodigo(), depto.getDescricao());
    }

    public DepartamentoResponseDTO editar(Long id, DepartamentoRequestDTO request) {
        Departamento deptoExistente = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado para edição!"));

        deptoExistente.setCodigo(request.getCodigo());
        deptoExistente.setDescricao(request.getDescricao());

        Departamento deptoAtualizado = departamentoRepository.save(deptoExistente);

        return new DepartamentoResponseDTO(deptoAtualizado.getId(), deptoAtualizado.getCodigo(), deptoAtualizado.getDescricao());
    }

    public void deletar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento não encontrado para exclusão!");
        }
        departamentoRepository.deleteById(id);
    }
}