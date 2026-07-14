package com.empresa.funcionarios.controller;

import com.empresa.funcionarios.dto.request.VinculoRequestDTO;
import com.empresa.funcionarios.dto.response.VinculoResponseDTO;
import com.empresa.funcionarios.service.VinculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vinculos")
@CrossOrigin(origins = "*")
public class VinculoController {

    @Autowired
    private VinculoService vinculoService;

    @GetMapping
    public ResponseEntity<List<VinculoResponseDTO>> listarPorFuncionario(@RequestParam Long funcionarioId) {
        return ResponseEntity.ok(vinculoService.listarPorFuncionario(funcionarioId));
    }

    @GetMapping("/todos")
    public ResponseEntity<List<VinculoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(vinculoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinculoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vinculoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VinculoResponseDTO> cadastrar(@Valid @RequestBody VinculoRequestDTO request) {
        VinculoResponseDTO response = vinculoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinculoResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody VinculoRequestDTO request) {
        return ResponseEntity.ok(vinculoService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vinculoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
