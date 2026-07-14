import { useState } from "react";
import Modal from "./Modal.jsx";
import cargoApi from "../services/cargoApi.js";
import { extrairMensagemErro } from "../services/api.js";

export default function FormCargo({ cargo, onClose, onSalvo }) {
  const editando = Boolean(cargo);
  const [codigo, setCodigo] = useState(cargo?.codigo ?? "");
  const [descricao, setDescricao] = useState(cargo?.descricao ?? "");
  const [erro, setErro] = useState("");
  const [salvando, setSalvando] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    setSalvando(true);

    try {
      const dados = { codigo, descricao };
      if (editando) {
        await cargoApi.editar(cargo.id, dados);
      } else {
        await cargoApi.cadastrar(dados);
      }
      onSalvo();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível salvar o cargo."));
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Modal title={editando ? "Editar Cargo" : "Novo Cargo"} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        {erro && <div className="error-banner">{erro}</div>}

        <div className="form-field">
          <label htmlFor="cargo-codigo">Código</label>
          <input
            id="cargo-codigo"
            value={codigo}
            onChange={(e) => setCodigo(e.target.value)}
            required
            disabled={editando}
          />
        </div>

        <div className="form-field">
          <label htmlFor="cargo-descricao">Descrição</label>
          <input
            id="cargo-descricao"
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
