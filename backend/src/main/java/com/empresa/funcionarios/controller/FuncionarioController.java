package com.empresa.funcionarios.controller;

import com.empresa.funcionarios.dto.request.FuncionarioRequestDTO;
import com.empresa.funcionarios.dto.response.FuncionarioResponseDTO;
import com.empresa.funcionarios.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) Long cargoId,
            @RequestParam(required = false) Long departamentoId
    ) {
        return ResponseEntity.ok(funcionarioService.listarComFiltros(nome, cpf, matricula, empresa, cargoId, departamentoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@Valid @RequestBody FuncionarioRequestDTO request) {
        FuncionarioResponseDTO response = funcionarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody FuncionarioRequestDTO request) {
        return ResponseEntity.ok(funcionarioService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
