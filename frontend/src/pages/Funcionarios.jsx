import { useEffect, useState } from "react";
import TabelaFuncionarios from "../components/TabelaFuncionarios.jsx";
import FormFuncionario from "../components/FormFuncionario.jsx";
import ModalVinculos from "../components/ModalVinculos.jsx";
import funcionarioApi from "../services/funcionarioApi.js";
import cargoApi from "../services/cargoApi.js";
import departamentoApi from "../services/departamentoApi.js";
import relatorioApi from "../services/relatorioApi.js";
import { extrairMensagemErro } from "../services/api.js";

const FILTROS_INICIAIS = {
  nome: "",
  cpf: "",
  matricula: "",
  empresa: "",
  cargoId: "",
  departamentoId: "",
};

export default function Funcionarios() {
  const [funcionarios, setFuncionarios] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);
  const [filtros, setFiltros] = useState(FILTROS_INICIAIS);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [baixando, setBaixando] = useState(false);

  const [funcionarioEmEdicao, setFuncionarioEmEdicao] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [funcionarioVinculos, setFuncionarioVinculos] = useState(null);

  useEffect(() => {
    cargoApi.listar().then(setCargos).catch(() => {});
    departamentoApi.listar().then(setDepartamentos).catch(() => {});
  }, []);

  useEffect(() => {
    const debounce = setTimeout(() => carregar(), 300);
    return () => clearTimeout(debounce);
  }, [filtros]);

  async function carregar() {
    setCarregando(true);
    setErro("");
    try {
      const dados = await funcionarioApi.listar(filtros);
      setFuncionarios(dados);
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os funcionários."));
    } finally {
      setCarregando(false);
    }
  }

  function atualizarFiltro(campo, valor) {
    setFiltros((atual) => ({ ...atual, [campo]: valor }));
  }

  function abrirNovo() {
    setFuncionarioEmEdicao(null);
    setMostrarForm(true);
  }

  function abrirEdicao(funcionario) {
    setFuncionarioEmEdicao(funcionario);
    setMostrarForm(true);
  }

  function aoSalvar() {
    setMostrarForm(false);
    carregar();
  }

  async function baixarRelatorio() {
    setBaixando(true);
    try {
      await relatorioApi.baixarRelatorioFuncionarios();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível gerar o relatório."));
    } finally {
      setBaixando(false);
    }
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Funcionários</h1>
          <p className="page-header__subtitle">Veja os funcionários cadastrados no sistema.</p>
        </div>
        <div className="page-header__actions">
          <button className="btn" onClick={baixarRelatorio} disabled={baixando}>
            <span className="material-symbols-outlined">download</span>
            {baixando ? "Gerando..." : "Baixar Relatório"}
          </button>
          <button className="btn btn--solid" onClick={abrirNovo}>
            <span className="material-symbols-outlined">add</span> Novo Funcionário
          </button>
        </div>
      </div>

      {erro && <div className="error-banner">{erro}</div>}

      <div className="filter-card">
        <div className="filter-field">
          <label>Nome do Funcionário</label>
          <input
            placeholder="Procure pelo funcionário"
            value={filtros.nome}
            onChange={(e) => atualizarFiltro("nome", e.target.value)}
          />
        </div>
        <div className="filter-field">
          <label>CPF</label>
          <input
            placeholder="000.000.000-00"
            value={filtros.cpf}
            onChange={(e) => atualizarFiltro("cpf", e.target.value)}
          />
        </div>
        <div className="filter-field">
          <label>Matrícula</label>
          <input
            placeholder="0000000000"
            value={filtros.matricula}
            onChange={(e) => atualizarFiltro("matricula", e.target.value)}
          />
        </div>
        <div className="filter-field">
          <label>Empresa</label>
          <input
            placeholder="Procure pela empresa"
            value={filtros.empresa}
            onChange={(e) => atualizarFiltro("empresa", e.target.value)}
          />
        </div>
        <div className="filter-field">
          <label>Cargo</label>
          <select value={filtros.cargoId} onChange={(e) => atualizarFiltro("cargoId", e.target.value)}>
            <option value="">Selecione uma opção</option>
            {cargos.map((c) => (
              <option key={c.id} value={c.id}>
                {c.codigo} — {c.descricao}
              </option>
            ))}
          </select>
        </div>
        <div className="filter-field">
          <label>Departamento</label>
          <select
            value={filtros.departamentoId}
            onChange={(e) => atualizarFiltro("departamentoId", e.target.value)}
          >
            <option value="">Selecione uma opção</option>
            {departamentos.map((d) => (
              <option key={d.id} value={d.id}>
                {d.codigo} — {d.descricao}
              </option>
            ))}
          </select>
        </div>
      </div>

      <p className="filter-hint">Clique para ver os vínculos de empresa do funcionário</p>

      {carregando ? (
        <div className="loading-state">Carregando...</div>
      ) : (
        <TabelaFuncionarios
          funcionarios={funcionarios}
          onEditar={abrirEdicao}
          onVerVinculos={setFuncionarioVinculos}
        />
      )}

      {mostrarForm && (
        <FormFuncionario
          funcionario={funcionarioEmEdicao}
          onClose={() => setMostrarForm(false)}
          onSalvo={aoSalvar}
        />
      )}

      {funcionarioVinculos && (
        <ModalVinculos
          funcionario={funcionarioVinculos}
          onClose={() => setFuncionarioVinculos(null)}
        />
      )}
    </>
  );
}
