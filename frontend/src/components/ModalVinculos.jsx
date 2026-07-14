import { useEffect, useState } from "react";
import Modal from "./Modal.jsx";
import vinculoApi from "../services/vinculoApi.js";
import cargoApi from "../services/cargoApi.js";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";

export default function ModalVinculos({ funcionario, onClose }) {
  const [vinculos, setVinculos] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [mostrarForm, setMostrarForm] = useState(false);

  const [empresa, setEmpresa] = useState("");
  const [matricula, setMatricula] = useState("");
  const [cargoId, setCargoId] = useState("");
  const [departamentoId, setDepartamentoId] = useState("");
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    setCarregando(true);
    setErro("");
    try {
      const [listaVinculos, listaCargos, listaDepartamentos] = await Promise.all([
        vinculoApi.listarPorFuncionario(funcionario.id),
        cargoApi.listar(),
        departamentoApi.listar(),
      ]);
      setVinculos(listaVinculos);
      setCargos(listaCargos);
      setDepartamentos(listaDepartamentos);
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os vínculos."));
    } finally {
      setCarregando(false);
    }
  }

  async function handleAdicionar(e) {
    e.preventDefault();
    setErro("");
    setSalvando(true);
    try {
      await vinculoApi.cadastrar({
        empresa,
        matricula,
        funcionarioId: funcionario.id,
        cargoId: Number(cargoId),
        departamentoId: Number(departamentoId),
      });
      setEmpresa("");
      setMatricula("");
      setCargoId("");
      setDepartamentoId("");
      setMostrarForm(false);
      await carregarTudo();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível adicionar o vínculo."));
    } finally {
      setSalvando(false);
    }
  }

  async function handleExcluir(id) {
    try {
      await vinculoApi.excluir(id);
      setVinculos((atual) => atual.filter((v) => v.id !== id));
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível remover o vínculo."));
    }
  }

  return (
    <Modal
      title={`Vínculos de ${funcionario.nome}`}
      onClose={onClose}
      wide
      footer={
        <button className="btn btn--ghost" onClick={onClose}>
          Fechar
        </button>
      }
    >
      {erro && <div className="error-banner">{erro}</div>}

      {carregando ? (
        <div className="loading-state">Carregando vínculos...</div>
      ) : (
        <>
          {vinculos.length === 0 && !mostrarForm && (
            <div className="empty-state">Nenhum vínculo de empresa cadastrado.</div>
          )}

          {vinculos.map((v) => (
            <div className="vinculo-item" key={v.id}>
              <div className="vinculo-item__info">
                <strong>{v.empresa}</strong>
                <span>
                  Matrícula {v.matricula} · {v.cargoCodigo} · {v.departamentoNome}
                </span>
              </div>
              <button className="btn btn--danger" onClick={() => handleExcluir(v.id)}>
                <span className="material-symbols-outlined">delete</span> Remover
              </button>
            </div>
          ))}

          {mostrarForm ? (
            <form onSubmit={handleAdicionar} style={{ marginTop: 16 }}>
              <div className="form-row">
                <div className="form-field">
                  <label>Empresa</label>
                  <input value={empresa} onChange={(e) => setEmpresa(e.target.value)} required />
                </div>
                <div className="form-field">
                  <label>Matrícula</label>
                  <input value={matricula} onChange={(e) => setMatricula(e.target.value)} required />
                </div>
              </div>

              <div className="form-row">
                <div className="form-field">
                  <label>Cargo</label>
                  <select value={cargoId} onChange={(e) => setCargoId(e.target.value)} required>
                    <option value="">Selecione uma opção</option>
                    {cargos.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.codigo} — {c.descricao}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-field">
                  <label>Departamento</label>
                  <select
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

              <div style={{ display: "flex", gap: 10, justifyContent: "flex-end" }}>
                <button
                  type="button"
                  className="btn btn--ghost"
                  onClick={() => setMostrarForm(false)}
                >
                  Cancelar
                </button>
                <button type="submit" className="btn btn--solid" disabled={salvando}>
                  {salvando ? "Salvando..." : "Adicionar vínculo"}
                </button>
              </div>
            </form>
          ) : (
            <button className="btn" style={{ marginTop: 8 }} onClick={() => setMostrarForm(true)}>
              <span className="material-symbols-outlined">add</span> Adicionar vínculo
            </button>
          )}
        </>
      )}
    </Modal>
  );
}
