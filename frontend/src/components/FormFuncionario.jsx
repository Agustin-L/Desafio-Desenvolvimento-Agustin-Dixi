import { useState } from "react";
import Modal from "./Modal.jsx";
import funcionarioApi from "../services/funcionarioApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { formatarCpf, limparCpf, cpfValido } from "../utils/cpf.js";

export default function FormFuncionario({ funcionario, onClose, onSalvo }) {
  const editando = Boolean(funcionario);
  const [nome, setNome] = useState(funcionario?.nome ?? "");
  const [cpf, setCpf] = useState(formatarCpf(funcionario?.cpf ?? ""));
  const [erro, setErro] = useState("");
  const [erros, setErros] = useState({ nome: "", cpf: "" });
  const [salvando, setSalvando] = useState(false);

  function handleNomeChange(e) {
    const valor = e.target.value.replace(/[0-9]/g, "").replace(/\s{2,}/g, " ");
    setNome(valor);
    if (erros.nome) setErros((prev) => ({ ...prev, nome: "" }));
  }

  function handleCpfChange(e) {
    setCpf(formatarCpf(e.target.value));
    if (erros.cpf) setErros((prev) => ({ ...prev, cpf: "" }));
  }

  function validar() {
    const novosErros = { nome: "", cpf: "" };

    if (!nome.trim()) {
      novosErros.nome = "Informe o nome do funcionário.";
    } else if (nome.trim().length < 3) {
      novosErros.nome = "O nome deve ter pelo menos 3 caracteres.";
    }

    if (!limparCpf(cpf)) {
      novosErros.cpf = "Informe o CPF.";
    } else if (limparCpf(cpf).length < 11) {
      novosErros.cpf = "O CPF deve ter 11 dígitos.";
    } else if (!cpfValido(cpf)) {
      novosErros.cpf = "CPF inválido.";
    }

    setErros(novosErros);
    return !novosErros.nome && !novosErros.cpf;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");

    if (!validar()) return;

    setSalvando(true);
    try {
      const dados = { nome: nome.trim(), cpf: limparCpf(cpf) };
      if (editando) {
        await funcionarioApi.editar(funcionario.id, dados);
      } else {
        await funcionarioApi.cadastrar(dados);
      }
      onSalvo();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível salvar o funcionário."));
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Modal title={editando ? "Editar Funcionário" : "Novo Funcionário"} onClose={onClose}>
      <form onSubmit={handleSubmit} noValidate>
        {erro && <div className="error-banner" role="alert">{erro}</div>}

        <div className={"form-field" + (erros.nome ? " form-field--invalid" : "")}>
          <label htmlFor="func-nome">Nome</label>
          <input
            id="func-nome"
            value={nome}
            onChange={handleNomeChange}
            placeholder="Ex.: Maria da Silva Santos"
            autoComplete="off"
            required
            aria-describedby="func-nome-ajuda"
            aria-invalid={Boolean(erros.nome)}
          />
          {erros.nome ? (
            <span id="func-nome-ajuda" className="field-error" role="alert">
              {erros.nome}
            </span>
          ) : (
            <span id="func-nome-ajuda" className="field-hint">
              Nome completo do funcionário, sem números.
            </span>
          )}
        </div>

        <div className={"form-field" + (erros.cpf ? " form-field--invalid" : "")}>
          <label htmlFor="func-cpf">CPF</label>
          <input
            id="func-cpf"
            value={cpf}
            onChange={handleCpfChange}
            placeholder="000.000.000-00"
            inputMode="numeric"
            autoComplete="off"
            maxLength={14}
            required
            aria-describedby="func-cpf-ajuda"
            aria-invalid={Boolean(erros.cpf)}
          />
          {erros.cpf ? (
            <span id="func-cpf-ajuda" className="field-error" role="alert">
              {erros.cpf}
            </span>
          ) : (
            <span id="func-cpf-ajuda" className="field-hint">
              Digite apenas os números — a máscara é aplicada automaticamente.
            </span>
          )}
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
