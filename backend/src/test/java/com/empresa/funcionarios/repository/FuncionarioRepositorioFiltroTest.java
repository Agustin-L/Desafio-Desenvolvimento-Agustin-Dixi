package com.empresa.funcionarios.repository;

import com.empresa.funcionarios.model.Cargo;
import com.empresa.funcionarios.model.Departamento;
import com.empresa.funcionarios.model.Funcionario;
import com.empresa.funcionarios.model.Vinculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// exercita a JPQL buscarComFiltros num H2 em memória (o banco de arquivo é substituído automaticamente)
@DataJpaTest
class FuncionarioRepositorioFiltroTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private TestEntityManager em;

    private Cargo cargoAnalista;
    private Cargo cargoGerente;
    private Departamento deptoTi;
    private Departamento deptoRh;

    // cenário: Ana tem 2 vínculos (Dixi/1000/Analista/TI e Beta/2000/Gerente/RH),
    // Bruno tem 1 (Dixi/3000/Gerente/TI) e Carla está inativa e sem vínculo
    @BeforeEach
    void cenario() {
        cargoAnalista = cargo("001", "Analista");
        cargoGerente = cargo("002", "Gerente");
        deptoTi = departamento("D1", "TI");
        deptoRh = departamento("D2", "RH");

        Funcionario ana = funcionario("Ana", "529.982.247-25", true);
        Funcionario bruno = funcionario("Bruno", "123.456.789-09", true);
        funcionario("Carla", "111.444.777-35", false);

        vinculo(ana, "Dixi Tecnologia", "1000", cargoAnalista, deptoTi);
        vinculo(ana, "Beta Corp", "2000", cargoGerente, deptoRh);
        vinculo(bruno, "Dixi Tecnologia", "3000", cargoGerente, deptoTi);
    }

    private Cargo cargo(String codigo, String descricao) {
        Cargo c = new Cargo();
        c.setCodigo(codigo);
        c.setDescricao(descricao);
        return em.persist(c);
    }

    private Departamento departamento(String codigo, String descricao) {
        Departamento d = new Departamento();
        d.setCodigo(codigo);
        d.setDescricao(descricao);
        return em.persist(d);
    }

    private Funcionario funcionario(String nome, String cpf, boolean ativo) {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setCpf(cpf);
        f.setAtivo(ativo);
        return em.persist(f);
    }

    private void vinculo(Funcionario f, String empresa, String matricula, Cargo c, Departamento d) {
        Vinculo v = new Vinculo();
        v.setFuncionario(f);
        v.setEmpresa(empresa);
        v.setMatricula(matricula);
        v.setCargo(c);
        v.setDepartamento(d);
        em.persist(v);
    }

    private List<String> nomes(List<Funcionario> lista) {
        return lista.stream().map(Funcionario::getNome).toList();
    }

    @Test
    void semFiltrosRetornaTodosOrdenadosPorNomeSemDuplicar() {
        List<Funcionario> resultado =
                funcionarioRepository.buscarComFiltros(null, null, null, null, null, null, null);

        assertThat(nomes(resultado)).containsExactly("Ana", "Bruno", "Carla");
    }

    @Test
    void filtraPorNomeParcialIgnorandoMaiusculas() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros("aNa", null, null, null, null, null, null)))
                .containsExactly("Ana");
    }

    @Test
    void filtraPorCpfParcial() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, "529", null, null, null, null, null)))
                .containsExactly("Ana");
    }

    @Test
    void filtraPorMatricula() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, "3000", null, null, null, null)))
                .containsExactly("Bruno");
    }

    // "000" bate nas matrículas 1000, 2000 e 3000; Ana tem duas mas aparece uma vez (DISTINCT)
    @Test
    void filtraPorMatriculaParcialSemDuplicarFuncionario() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, "000", null, null, null, null)))
                .containsExactly("Ana", "Bruno");
    }

    @Test
    void filtraPorEmpresaIgnorandoMaiusculas() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, "dixi", null, null, null)))
                .containsExactly("Ana", "Bruno");
    }

    @Test
    void filtraPorCargo() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, null, cargoGerente.getId(), null, null)))
                .containsExactly("Ana", "Bruno");
    }

    @Test
    void filtraPorDepartamento() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, null, null, deptoRh.getId(), null)))
                .containsExactly("Ana");
    }

    @Test
    void filtraPorSituacaoAtivoEInativo() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, null, null, null, true)))
                .containsExactly("Ana", "Bruno");
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, null, null, null, false)))
                .containsExactly("Carla");
    }

    // Ana tem empresa Dixi (como Analista) e cargo Gerente (na Beta), mas não os dois no mesmo vínculo
    @Test
    void combinaFiltrosSobreOMesmoVinculo() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros(null, null, null, "Dixi", cargoGerente.getId(), null, null)))
                .containsExactly("Bruno");
    }

    @Test
    void funcionarioSemVinculoNaoApareceAoFiltrarPorDadosDeVinculo() {
        assertThat(nomes(funcionarioRepository.buscarComFiltros("Carla", null, "1000", null, null, null, null)))
                .isEmpty();
    }
}
