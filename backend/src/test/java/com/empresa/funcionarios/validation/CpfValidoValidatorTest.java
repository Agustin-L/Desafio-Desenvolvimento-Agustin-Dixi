package com.empresa.funcionarios.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CpfValidoValidatorTest {

    private final CpfValidoValidator validator = new CpfValidoValidator();

    @Test
    void cpfNuloOuEmBrancoEhInvalido() {
        assertThat(validator.isValid(null, null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
        assertThat(validator.isValid("   ", null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "5299822472", "529982247251"})
    void cpfComTamanhoDiferenteDe11DigitosEhInvalido(String cpf) {
        assertThat(validator.isValid(cpf, null)).isFalse();
    }

    @Test
    void cpfComTodosOsDigitosIguaisEhInvalido() {
        assertThat(validator.isValid("111.111.111-11", null)).isFalse();
        assertThat(validator.isValid("00000000000", null)).isFalse();
    }

    // os dígitos verificadores corretos seriam -25
    @Test
    void cpfComDigitoVerificadorErradoEhInvalido() {
        assertThat(validator.isValid("529.982.247-24", null)).isFalse();
        assertThat(validator.isValid("529.982.247-15", null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"529.982.247-25", "52998224725", "123.456.789-09", "111.444.777-35"})
    void cpfValidoEhAceitoComOuSemMascara(String cpf) {
        assertThat(validator.isValid(cpf, null)).isTrue();
    }
}
