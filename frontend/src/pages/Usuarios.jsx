import { useEffect, useState } from "react";
import usuarioApi from "../services/usuarioApi.js";
import { extrairMensagemErro } from "../services/api.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");

  const [username, setUsername] = useState("");
  const [senha, setSenha] = useState("");
  const [perfil, setPerfil] = useState("PADRAO");
  const [salvando, setSalvando] = useState(false);

  const { usuario: usuarioLogado } = useAuth();

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    setCarregando(true);
    setErro("");
    try {
      setUsuarios(await usuarioApi.listar());
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível carregar os usuários."));
    } finally {
      setCarregando(false);
    }
  }

  async function handleCriar(e) {
    e.preventDefault();
    setErro("");
    setSalvando(true);
    try {
      await usuarioApi.cadastrar({ username, senha, perfil });
      setUsername("");
      setSenha("");
      setPerfil("PADRAO");
      await carregar();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível criar o usuário."));
    } finally {
      setSalvando(false);
    }
  }

  async function handleExcluir(id) {
    setErro("");
    try {
      await usuarioApi.excluir(id);
      await carregar();
    } catch (err) {
      setErro(extrairMensagemErro(err, "Não foi possível excluir o usuário."));
    }
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1 className="page-header__title">Usuários</h1>
          <p className="page-header__subtitle">
            Somente administradores podem criar novos acessos e excluir outros usuários.
          </p>
        </div>
      </div>

      {erro && <div className="error-banner">{erro}</div>}

      <div className="table-card" style={{ marginBottom: 24 }}>
        <form onSubmit={handleCriar} className="form-row" style={{ padding: 18, alignItems: "flex-end", flexWrap: "wrap" }}>
          <div className="form-field" style={{ marginBottom: 0 }}>
            <label>Usuário</label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="form-field" style={{ marginBottom: 0 }}>
            <label>Senha</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
              minLength={6}
              placeholder="mínimo 6 caracteres"
            />
          </div>
          <div className="form-field" style={{ marginBottom: 0 }}>
            <label>Perfil</label>
            <select value={perfil} onChange={(e) => setPerfil(e.target.value)}>
              <option value="PADRAO">Padrão</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>
          <button type="submit" className="btn btn--solid" disabled={salvando}>
            {salvando ? (
              "Criando..."
            ) : (
              <>
                <span className="material-symbols-outlined">add</span> Novo Usuário
              </>
            )}
          </button>
        </form>
      </div>

      {carregando ? (
        <div className="loading-state">Carregando...</div>
      ) : usuarios.length === 0 ? (
        <div className="table-card">
          <div className="empty-state">Nenhum usuário cadastrado.</div>
        </div>
      ) : (
        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th>Usuário</th>
                <th>Perfil</th>
                <th className="col-icon">Excluir</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((u) => (
                <tr key={u.id}>
                  <td>{u.username}</td>
                  <td>{u.perfil === "ADMIN" ? "Administrador" : "Padrão"}</td>
                  <td>
                    <button
                      className="btn btn--danger"
                      disabled={u.username === usuarioLogado?.username}
                      title={
                        u.username === usuarioLogado?.username
                          ? "Você não pode excluir seu próprio acesso"
                          : "Excluir usuário"
                      }
                      onClick={() => handleExcluir(u.id)}
                    >
                      <span className="material-symbols-outlined">delete</span> Excluir
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
