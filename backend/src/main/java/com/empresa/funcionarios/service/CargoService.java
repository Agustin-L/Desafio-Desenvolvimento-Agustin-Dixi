package com.empresa.funcionarios.service;

import com.empresa.funcionarios.dto.request.CargoRequestDTO;
import com.empresa.funcionarios.dto.response.CargoResponseDTO;
import com.empresa.funcionarios.exception.RecursoNaoEncontradoException;
import com.empresa.funcionarios.exception.RegraDeNegocioException;
import com.empresa.funcionarios.model.Cargo;
import com.empresa.funcionarios.repository.CargoRepository;
import com.empresa.funcionarios.repository.VinculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private VinculoRepository vinculoRepository;

    public List<CargoResponseDTO> listarTodos() {
        return cargoRepository.findAll().stream()
                .map(cargo -> new CargoResponseDTO(cargo.getId(), cargo.getCodigo(), cargo.getDescricao()))
                .toList();
    }

    public CargoResponseDTO salvar(CargoRequestDTO request) {
        if (cargoRepository.existsByCodigo(request.getCodigo())) {
            throw new RegraDeNegocioException("Já existe um cargo cadastrado com este código!");
        }

        Cargo cargoModel = new Cargo();
        cargoModel.setCodigo(request.getCodigo());
        cargoModel.setDescricao(request.getDescricao());

        Cargo cargoSalvo = cargoRepository.save(cargoModel);

        return new CargoResponseDTO(cargoSalvo.getId(), cargoSalvo.getCodigo(), cargoSalvo.getDescricao());
    }

    public CargoResponseDTO buscarPorId(Long id) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cargo não encontrado com o ID: " + id));

        return new CargoResponseDTO(cargo.getId(), cargo.getCodigo(), cargo.getDescricao());
    }

    public CargoResponseDTO editar(Long id, CargoRequestDTO request) {
        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cargo não encontrado para edição!"));

        cargoExistente.setDescricao(request.getDescricao());

        Cargo cargoAtualizado = cargoRepository.save(cargoExistente);

        return new CargoResponseDTO(cargoAtualizado.getId(), cargoAtualizado.getCodigo(), cargoAtualizado.getDescricao());
    }

    public void deletar(Long id) {
        if (!cargoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cargo não encontrado para exclusão!");
        }
        if (vinculoRepository.existsByCargoId(id)) {
            throw new RegraDeNegocioException("Não é possível excluir o cargo: existem vínculos de funcionários que o utilizam!");
        }
        cargoRepository.deleteById(id);
    }
}