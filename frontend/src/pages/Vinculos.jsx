import { useEffect, useMemo, useState } from "react";
import TabelaVinculos from "../components/TabelaVinculos.jsx";
import FormVinculo from "../components/FormVinculo.jsx";
import vinculoApi from "../services/vinculoApi.js";
import funcionarioApi from "../services/funcionarioApi.js";
import cargoApi from "../services/cargoApi.js";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";

const FILTROS_INICIAIS = {
  nome: "",
  empresa: "",
  cargoId: "",
  departamentoId: "",
};

export default function Vinculos() {
  const [vinculos, setVinculos] = useState([]);
  const [funcionarios, setFuncionarios] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);
  const [filtros, setFiltros] = useState(FILTROS_INICIAIS);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [mostrarForm, setMostrarForm] = useState(false);

  useEffect(() => {
    funcionarioApi.listar().then(setFuncionarios).catch(() => {});
    cargoApi.listar().then(setCargos).catch(() => {});
    departamentoApi.listar().then(setDepartamentos).catch(() => {});
    carregar();
  }, []);

  async function carregar() {
    setCarregando(true);
    setErro("");
    try {
      const dados = await vinculoApi.listarTodos();
      setVinculos(dados);
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os vínculos."));
    } finally {
      setCarregando(false);
    }
  }

  function atualizarFiltro(campo, valor) {
    setFiltros((atual) => ({ ...atual, [campo]: valor }));
  }

  function aoSalvar() {
    setMostrarForm(false);
    carregar();
  }

  async function handleDesvincular(vinculo) {
    const confirmar = window.confirm(
      `Desvincular ${vinculo.funcionarioNome} de ${vinculo.empresa}?`
    );
    if (!confirmar) return;

    try {
      await vinculoApi.excluir(vinculo.id);
      setVinculos((atual) => atual.filter((v) => v.id !== vinculo.id));
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível desvincular o funcionário."));
    }
  }

  const vinculosFiltrados = useMemo(() => {
    return vinculos.filter((v) => {
      const bateNome = !filtros.nome || v.funcionarioNome?.toLowerCase().includes(filtros.nome.toLowerCase());
      const bateEmpresa = !filtros.empresa || v.empresa?.toLowerCase().includes(filtros.empresa.toLowerCase());
      const bateCargo = !filtros.cargoId || String(v.cargoId) === String(filtros.cargoId);
      const bateDepartamento =
        !filtros.departamentoId || String(v.departamentoId) === String(filtros.departamentoId);
      return bateNome && bateEmpresa && bateCargo && bateDepartamento;
    });
  }, [vinculos, filtros]);

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Vínculos</h1>
          <p className="page-header__subtitle">
            Vincule ou desvincule funcionários de uma empresa e cargo.
          </p>
        </div>
        <div className="page-header__actions">
          <button className="btn btn--solid" onClick={() => setMostrarForm(true)}>
            <span className="material-symbols-outlined">add</span> Vincular Funcionário
          </button>
        </div>
      </div>

      {erro && <div className="error-banner">{erro}</div>}

      <div className="filter-card">
        <div className="filter-field">
          <label>Funcionário</label>
          <input
            placeholder="Procure pelo funcionário"
            value={filtros.nome}
            onChange={(e) => atualizarFiltro("nome", e.target.value)}
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

      <p className="filter-hint">Clique no switch para desvincular o funcionário.</p>

      {carregando ? (
        <div className="loading-state">Carregando...</div>
      ) : (
        <TabelaVinculos vinculos={vinculosFiltrados} onDesvincular={handleDesvincular} />
      )}

      {mostrarForm && (
        <FormVinculo
          funcionarios={funcionarios}
          cargos={cargos}
          departamentos={departamentos}
          onClose={() => setMostrarForm(false)}
          onSalvo={aoSalvar}
        />
      )}
    </>
  );
}
