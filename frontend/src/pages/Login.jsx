import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import authApi from "../services/authApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Login() {
  const [username, setUsername] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [entrando, setEntrando] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    setEntrando(true);

    try {
      const dados = await authApi.login({ username, senha });
      login(dados);
      const destino = location.state?.from || "/funcionarios";
      navigate(destino, { replace: true });
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível fazer login."));
    } finally {
      setEntrando(false);
    }
  }

  return (
    <div className="login-page">
      <form className="login-card" onSubmit={handleSubmit}>
        <div className="login-card__logo">X</div>
        <h1>Dixi</h1>
        <p>Entre com seu usuário e senha para continuar.</p>

        {erro && <div className="error-banner">{erro}</div>}

        <div className="form-field">
          <label htmlFor="login-username">Usuário</label>
          <input
            id="login-username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            autoFocus
          />
        </div>

        <div className="form-field">
          <label htmlFor="login-senha">Senha</label>
          <input
            id="login-senha"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
        </div>

        <button
          type="submit"
          className="btn btn--solid"
          disabled={entrando}
          style={{ width: "100%", justifyContent: "center", marginTop: 4 }}
        >
          {entrando ? "Entrando..." : "Entrar"}
        </button>
      </form>
    </div>
  );
}
