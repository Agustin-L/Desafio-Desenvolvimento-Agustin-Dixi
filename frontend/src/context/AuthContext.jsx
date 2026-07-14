import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(() => {
    const salvo = localStorage.getItem("dixi_usuario");
    return salvo ? JSON.parse(salvo) : null;
  });

  function login(dados) {
    localStorage.setItem("dixi_token", dados.token);
    const usuarioLogado = { username: dados.username, perfil: dados.perfil };
    localStorage.setItem("dixi_usuario", JSON.stringify(usuarioLogado));
    setUsuario(usuarioLogado);
  }

  function logout() {
    localStorage.removeItem("dixi_token");
    localStorage.removeItem("dixi_usuario");
    setUsuario(null);
  }

  const isAdmin = usuario?.perfil === "ADMIN";

  return (
    <AuthContext.Provider value={{ usuario, login, logout, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth precisa ser usado dentro de um AuthProvider");
  }
  return context;
}
