import { useState } from "react";
import Modal from "./Modal.jsx";
import vinculoApi from "../services/vinculoApi.js";
import { extrairMensagemErro } from "../services/api.js";

export default function FormVinculo({ funcionarios, cargos, departamentos, onClose, onSalvo }) {
  const [funcionarioId, setFuncionarioId] = useState("");
  const [empresa, setEmpresa] = useState("");
  const [matricula, setMatricula] = useState("");
  const [cargoId, setCargoId] = useState("");
  const [departamentoId, setDepartamentoId] = useState("");
  const [erro, setErro] = useState("");
  const [salvando, setSalvando] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    setSalvando(true);
    try {
      await vinculoApi.cadastrar({
        empresa,
        matricula,
        funcionarioId: Number(funcionarioId),
        cargoId: Number(cargoId),
        departamentoId: Number(departamentoId),
      });
      onSalvo();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível vincular o funcionário."));
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Modal title="Vincular Funcionário" onClose={onClose}>
      <form onSubmit={handleSubmit}>
        {erro && <div className="error-banner">{erro}</div>}

        <div className="form-field">
          <label htmlFor="vinculo-funcionario">Funcionário</label>
          <select
            id="vinculo-funcionario"
            value={funcionarioId}
            onChange={(e) => setFuncionarioId(e.target.value)}
            required
          >
            <option value="">Selecione uma opção</option>
            {funcionarios.map((f) => (
              <option key={f.id} value={f.id}>
                {f.nome}
              </option>
            ))}
          </select>
        </div>

        <div className="form-row">
          <div className="form-field">
            <label htmlFor="vinculo-empresa">Empresa</label>
            <input
              id="vinculo-empresa"
              value={empresa}
              onChange={(e) => setEmpresa(e.target.value)}
              placeholder="Escreva algo"
              required
            />
          </div>
          <div className="form-field">
            <label htmlFor="vinculo-matricula">Matrícula</label>
            <input
              id="vinculo-matricula"
              value={matricula}
              onChange={(e) => setMatricula(e.target.value.replace(/\D/g, ""))}
              placeholder="0000000000"
              inputMode="numeric"
              required
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-field">
            <label htmlFor="vinculo-cargo">Cargo</label>
            <select
              id="vinculo-cargo"
              value={cargoId}
              onChange={(e) => setCargoId(e.target.value)}
              required
            >
              <option value="">Selecione uma opção</option>
              {cargos.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.codigo} — {c.descricao}
                </option>
              ))}
            </select>
          </div>
          <div className="form-field">
            <label htmlFor="vinculo-departamento">Departamento</label>
            <select
              id="vinculo-departamento"
              value={departamentoId}
              onChange={(e) => setDepartamentoId(e.target.value)}
              required
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

        <div className="modal-footer" style={{ padding: 0, border: "none", marginTop: 20 }}>
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--solid" disabled={salvando}>
            {salvando ? "Salvando..." : "Vincular"}
          </button>
        </div>
      </form>
    </Modal>
  );
}
