package com.empresa.funcionarios.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidoValidator implements ConstraintValidator<CpfValido, String> {

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        String cpf = valor.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int digito1 = calcularDigito(cpf, 10, 9);
        if (digito1 != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        int digito2 = calcularDigito(cpf, 11, 10);
        return digito2 == Character.getNumericValue(cpf.charAt(10));
    }

    private int calcularDigito(String cpf, int fatorInicial, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (fatorInicial - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
