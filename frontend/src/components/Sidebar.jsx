import { NavLink } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

const linkClass = ({ isActive }) => "sidebar__link" + (isActive ? " active" : "");

export default function Sidebar() {
  const { usuario, isAdmin, logout } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar__logo">
        <span className="sidebar__logo-mark">X</span>
      </div>

      <nav className="sidebar__nav">
        <NavLink to="/funcionarios" className={linkClass}>
          <span className="material-symbols-outlined sidebar__icon">badge</span>
          <span>Funcionário</span>
        </NavLink>

        <NavLink to="/cargos" className={linkClass}>
          <span className="material-symbols-outlined sidebar__icon">work</span>
          <span>Cargo</span>
        </NavLink>

        <NavLink to="/departamentos" className={linkClass}>
          <span className="material-symbols-outlined sidebar__icon">apartment</span>
          <span>Departamento</span>
        </NavLink>

        <NavLink to="/vinculos" className={linkClass}>
          <span className="material-symbols-outlined sidebar__icon">link</span>
          <span>Vínculos</span>
        </NavLink>

        {isAdmin && (
          <NavLink to="/usuarios" className={linkClass}>
            <span className="material-symbols-outlined sidebar__icon">group</span>
            <span>Usuários</span>
          </NavLink>
        )}
      </nav>

      <div className="sidebar__footer">
        <span className="sidebar__user" title={usuario?.username}>
          {usuario?.username}
        </span>
        <button className="sidebar__logout" onClick={logout} title="Sair">
          <span className="material-symbols-outlined">logout</span>
          Sair
        </button>
      </div>
    </aside>
  );
}
