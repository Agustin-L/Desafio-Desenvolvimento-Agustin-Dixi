package com.empresa.funcionarios.controller;

import com.empresa.funcionarios.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/dados-gerais")
    public ResponseEntity<Map<String, Object>> obterDadosGerais() {
        return ResponseEntity.ok(relatorioService.obterDadosGerais());
    }

    @GetMapping("/funcionarios/csv")
    public ResponseEntity<byte[]> baixarRelatorioFuncionarios() {
        String csv = relatorioService.gerarRelatorioFuncionariosCsv();
        byte[] conteudo = csv.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-funcionarios.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(conteudo);
    }
}
