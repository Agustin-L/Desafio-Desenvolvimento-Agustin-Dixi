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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VinculoService {

    @Autowired
    private VinculoRepository vinculoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<VinculoResponseDTO> listarPorFuncionario(Long funcionarioId) {
        return vinculoRepository.findByFuncionarioId(funcionarioId).stream()
                .map(this::paraResponseDTO)
                .toList();
    }

    public List<VinculoResponseDTO> listarTodos() {
        return vinculoRepository.findAll().stream()
                .map(this::paraResponseDTO)
                .toList();
    }

    public VinculoResponseDTO salvar(VinculoRequestDTO request) {
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário informado não existe!"));

        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new RuntimeException("Cargo informado não existe!"));

        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento informado não existe!"));

        Vinculo vinculoModel = new Vinculo();
        vinculoModel.setEmpresa(request.getEmpresa());
        vinculoModel.setMatricula(request.getMatricula());
        vinculoModel.setFuncionario(funcionario);
        vinculoModel.setCargo(cargo);
        vinculoModel.setDepartamento(departamento);

        Vinculo vinculoSalvo = vinculoRepository.save(vinculoModel);

        return paraResponseDTO(vinculoSalvo);
    }

    public VinculoResponseDTO buscarPorId(Long id) {
        Vinculo vinculo = vinculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vínculo não encontrado com o ID: " + id));

        return paraResponseDTO(vinculo);
    }

    public VinculoResponseDTO editar(Long id, VinculoRequestDTO request) {
        Vinculo vinculoExistente = vinculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vínculo não encontrado para edição!"));

        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new RuntimeException("Cargo informado não existe!"));

        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento informado não existe!"));

        vinculoExistente.setEmpresa(request.getEmpresa());
        vinculoExistente.setMatricula(request.getMatricula());
        vinculoExistente.setCargo(cargo);
        vinculoExistente.setDepartamento(departamento);

        Vinculo vinculoAtualizado = vinculoRepository.save(vinculoExistente);

        return paraResponseDTO(vinculoAtualizado);
    }

    public void deletar(Long id) {
        if (!vinculoRepository.existsById(id)) {
            throw new RuntimeException("Vínculo não encontrado para exclusão!");
        }
        vinculoRepository.deleteById(id);
    }

    private VinculoResponseDTO paraResponseDTO(Vinculo vinculo) {
        return new VinculoResponseDTO(
                vinculo.getId(),
                vinculo.getEmpresa(),
                vinculo.getMatricula(),
                vinculo.getFuncionario().getId(),
                vinculo.getFuncionario().getNome(),
                vinculo.getCargo().getId(),
                vinculo.getCargo().getCodigo(),
                vinculo.getDepartamento().getId(),
                vinculo.getDepartamento().getDescricao()
        );
    }
}
