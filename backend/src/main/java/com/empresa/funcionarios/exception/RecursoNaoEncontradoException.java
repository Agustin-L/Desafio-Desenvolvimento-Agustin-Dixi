package com.empresa.funcionarios.exception;

// Estende RuntimeException para não quebrar chamadas/testes que tratam a hierarquia genérica
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
