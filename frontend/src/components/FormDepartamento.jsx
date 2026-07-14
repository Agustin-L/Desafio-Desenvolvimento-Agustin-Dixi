import { useState } from "react";
import Modal from "./Modal.jsx";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";

export default function FormDepartamento({ departamento, onClose, onSalvo }) {
  const editando = Boolean(departamento);
  const [codigo, setCodigo] = useState(departamento?.codigo ?? "");
  const [descricao, setDescricao] = useState(departamento?.descricao ?? "");
  const [erro, setErro] = useState("");
  const [salvando, setSalvando] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    setSalvando(true);

    try {
      const dados = { codigo, descricao };
      if (editando) {
        await departamentoApi.editar(departamento.id, dados);
      } else {
        await departamentoApi.cadastrar(dados);
      }
      onSalvo();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível salvar o departamento."));
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Modal title={editando ? "Editar Departamento" : "Novo Departamento"} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        {erro && <div className="error-banner">{erro}</div>}

        <div className="form-field">
          <label htmlFor="depto-codigo">Código</label>
          <input
            id="depto-codigo"
            value={codigo}
            onChange={(e) => setCodigo(e.target.value)}
            required
            disabled={editando}
          />
        </div>

        <div className="form-field">
          <label htmlFor="depto-descricao">Descrição</label>
          <input
            id="depto-descricao"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            required
          />
        </div>

        <div className="modal-footer" style={{ padding: 0, border: "none", marginTop: 20 }}>
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--solid" disabled={salvando}>
            {salvando ? "Salvando..." : "Salvar"}
          </button>
        </div>
      </form>
    </Modal>
  );
}
