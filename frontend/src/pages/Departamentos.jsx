import { useEffect, useState } from "react";
import TabelaDepartamentos from "../components/TabelaDepartamentos.jsx";
import FormDepartamento from "../components/FormDepartamento.jsx";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Departamentos() {
  const { isAdmin } = useAuth();
  const [departamentos, setDepartamentos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [departamentoEmEdicao, setDepartamentoEmEdicao] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    setCarregando(true);
    setErro("");
    try {
      const dados = await departamentoApi.listar();
      setDepartamentos(dados);
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os departamentos."));
    } finally {
      setCarregando(false);
    }
  }

  function abrirNovo() {
    setDepartamentoEmEdicao(null);
    setMostrarForm(true);
  }

  function abrirEdicao(departamento) {
    setDepartamentoEmEdicao(departamento);
    setMostrarForm(true);
  }

  function aoSalvar() {
    setMostrarForm(false);
    carregar();
  }

  async function handleExcluir(departamento) {
    const confirmado = window.confirm(
      `Excluir o departamento "${departamento.codigo} — ${departamento.descricao}"? Esta ação não pode ser desfeita.`
    );
    if (!confirmado) return;

    try {
      await departamentoApi.excluir(departamento.id);
      carregar();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível excluir o departamento."));
    }
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Departamentos</h1>
          <p className="page-header__subtitle">Veja os departamentos cadastrados no sistema.</p>
        </div>
        <div className="page-header__actions">
          <button className="btn" onClick={abrirNovo}>
            <span className="material-symbols-outlined" aria-hidden="true">add</span> Novo Departamento
          </button>
        </div>
      </div>

      {erro && <div className="error-banner" role="alert">{erro}</div>}

      {carregando ? (
        <div className="loading-state" role="status">Carregando...</div>
      ) : (
        <TabelaDepartamentos departamentos={departamentos} onEditar={abrirEdicao} onExcluir={isAdmin ? handleExcluir : undefined} />
      )}

      {mostrarForm && (
        <FormDepartamento
          departamento={departamentoEmEdicao}
          onClose={() => setMostrarForm(false)}
          onSalvo={aoSalvar}
        />
      )}
    </>
  );
}
