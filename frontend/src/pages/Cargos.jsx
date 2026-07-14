import { useEffect, useState } from "react";
import TabelaCargos from "../components/TabelaCargos.jsx";
import FormCargo from "../components/FormCargo.jsx";
import cargoApi from "../services/cargoApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Cargos() {
  const { isAdmin } = useAuth();
  const [cargos, setCargos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [cargoEmEdicao, setCargoEmEdicao] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    setCarregando(true);
    setErro("");
    try {
      const dados = await cargoApi.listar();
      setCargos(dados);
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os cargos."));
    } finally {
      setCarregando(false);
    }
  }

  function abrirNovo() {
    setCargoEmEdicao(null);
    setMostrarForm(true);
  }

  function abrirEdicao(cargo) {
    setCargoEmEdicao(cargo);
    setMostrarForm(true);
  }

  function aoSalvar() {
    setMostrarForm(false);
    carregar();
  }

  async function handleExcluir(cargo) {
    const confirmado = window.confirm(
      `Excluir o cargo "${cargo.codigo} — ${cargo.descricao}"? Esta ação não pode ser desfeita.`
    );
    if (!confirmado) return;

    try {
      await cargoApi.excluir(cargo.id);
      carregar();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível excluir o cargo."));
    }
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Cargos</h1>
          <p className="page-header__subtitle">Veja os cargos cadastrados no sistema.</p>
        </div>
        <div className="page-header__actions">
          <button className="btn" onClick={abrirNovo}>
            <span className="material-symbols-outlined" aria-hidden="true">add</span> Novo Cargo
          </button>
        </div>
      </div>

      {erro && <div className="error-banner" role="alert">{erro}</div>}

      {carregando ? (
        <div className="loading-state" role="status">Carregando...</div>
      ) : (
        <TabelaCargos cargos={cargos} onEditar={abrirEdicao} onExcluir={isAdmin ? handleExcluir : undefined} />
      )}

      {mostrarForm && (
        <FormCargo
          cargo={cargoEmEdicao}
          onClose={() => setMostrarForm(false)}
          onSalvo={aoSalvar}
        />
      )}
    </>
  );
}
