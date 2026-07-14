import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function RotaProtegida({ children, apenasAdmin = false }) {
  const { usuario, isAdmin } = useAuth();
  const location = useLocation();

  if (!usuario) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  if (apenasAdmin && !isAdmin) {
    return <Navigate to="/funcionarios" replace />;
  }

  return children;
}
