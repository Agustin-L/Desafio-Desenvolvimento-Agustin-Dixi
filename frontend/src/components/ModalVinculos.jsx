import { useEffect, useState } from "react";
import Modal from "./Modal.jsx";
import vinculoApi from "../services/vinculoApi.js";
import cargoApi from "../services/cargoApi.js";
import departamentoApi from "../services/departamentoApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function ModalVinculos({ funcionario, onClose }) {
  const { isAdmin } = useAuth();
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

  async function handleExcluir(vinculo) {
    const confirmar = window.confirm(
      `Remover o vínculo de ${funcionario.nome} com ${vinculo.empresa}?`
    );
    if (!confirmar) return;

    try {
      await vinculoApi.excluir(vinculo.id);
      setVinculos((atual) => atual.filter((v) => v.id !== vinculo.id));
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
      {erro && <div className="error-banner" role="alert">{erro}</div>}

      {carregando ? (
        <div className="loading-state" role="status">Carregando vínculos...</div>
      ) : (
        <>
          {vinculos.length === 0 && !mostrarForm && (
            <div className="empty-state">Nenhum vínculo de empresa cadastrado.</div>
          )}

          {vinculos.map((v) => {
            const cargoDescricao = cargos.find((c) => String(c.id) === String(v.cargoId))?.descricao;
            return (
              <div className="vinculo-item" key={v.id}>
                <div className="vinculo-item__info">
                  <strong>{v.empresa}</strong>
                  <span>
                    Matrícula {v.matricula} ·{" "}
                    {cargoDescricao ? `${v.cargoCodigo} — ${cargoDescricao}` : v.cargoCodigo} ·{" "}
                    {v.departamentoNome}
                  </span>
                </div>
                {isAdmin && (
                  <button
                    className="btn btn--danger"
                    aria-label={`Remover vínculo com ${v.empresa}`}
                    onClick={() => handleExcluir(v)}
                  >
                    <span className="material-symbols-outlined" aria-hidden="true">delete</span> Remover
                  </button>
                )}
              </div>
            );
          })}

          {mostrarForm ? (
            <form onSubmit={handleAdicionar} style={{ marginTop: 16 }}>
              <div className="form-row">
                <div className="form-field">
                  <label htmlFor="mv-empresa">Empresa</label>
                  <input
                    id="mv-empresa"
                    value={empresa}
                    onChange={(e) => setEmpresa(e.target.value)}
                    placeholder="Ex.: Dixi Tecnologia"
                    required
                    aria-describedby="mv-empresa-hint"
                  />
                  <span id="mv-empresa-hint" className="field-hint">
                    Nome da empresa contratante.
                  </span>
                </div>
                <div className="form-field">
                  <label htmlFor="mv-matricula">Matrícula</label>
                  <input
                    id="mv-matricula"
                    value={matricula}
                    onChange={(e) => setMatricula(e.target.value.replace(/\D/g, ""))}
                    placeholder="0000000000"
                    inputMode="numeric"
                    required
                    aria-describedby="mv-matricula-hint"
                  />
                  <span id="mv-matricula-hint" className="field-hint">
                    Somente números.
                  </span>
                </div>
              </div>

              <div className="form-row">
                <div className="form-field">
                  <label htmlFor="mv-cargo">Cargo</label>
                  <select
                    id="mv-cargo"
                    value={cargoId}
                    onChange={(e) => setCargoId(e.target.value)}
                    required
                    aria-describedby={cargos.length === 0 ? "mv-cargo-hint" : undefined}
                  >
                    <option value="">Selecione uma opção</option>
                    {cargos.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.codigo} — {c.descricao}
                      </option>
                    ))}
                  </select>
                  {cargos.length === 0 && (
                    <span id="mv-cargo-hint" className="field-hint">
                      Nenhum cargo cadastrado. Cadastre um na tela Cargos.
                    </span>
                  )}
                </div>
                <div className="form-field">
                  <label htmlFor="mv-departamento">Departamento</label>
                  <select
                    id="mv-departamento"
                    value={departamentoId}
                    onChange={(e) => setDepartamentoId(e.target.value)}
                    required
                    aria-describedby={departamentos.length === 0 ? "mv-departamento-hint" : undefined}
                  >
                    <option value="">Selecione uma opção</option>
                    {departamentos.map((d) => (
                      <option key={d.id} value={d.id}>
                        {d.codigo} — {d.descricao}
                      </option>
                    ))}
                  </select>
                  {departamentos.length === 0 && (
                    <span id="mv-departamento-hint" className="field-hint">
                      Nenhum departamento cadastrado. Cadastre um na tela Departamentos.
                    </span>
                  )}
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
              <span className="material-symbols-outlined" aria-hidden="true">add</span> Adicionar vínculo
            </button>
          )}
        </>
      )}
    </Modal>
  );
}
