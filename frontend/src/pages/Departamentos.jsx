import { useEffect, useState } from "react";
import TabelaDepartamentos from "../components/TabelaDepartamentos.jsx";
import FormDepartamento from "../components/FormDepartamento.jsx";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";

export default function Departamentos() {
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

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Departamentos</h1>
          <p className="page-header__subtitle">Veja os departamentos cadastrados no sistema.</p>
        </div>
        <div className="page-header__actions">
          <button className="btn" onClick={abrirNovo}>
            <span className="material-symbols-outlined">add</span> Novo Departamento
          </button>
        </div>
      </div>

      {erro && <div className="error-banner">{erro}</div>}

      {carregando ? (
        <div className="loading-state">Carregando...</div>
      ) : (
        <TabelaDepartamentos departamentos={departamentos} onEditar={abrirEdicao} />
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
