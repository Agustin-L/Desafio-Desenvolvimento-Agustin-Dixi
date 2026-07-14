import { Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext.jsx";
import RotaProtegida from "./components/RotaProtegida.jsx";
import Sidebar from "./components/Sidebar.jsx";
import Login from "./pages/Login.jsx";
import Funcionarios from "./pages/Funcionarios.jsx";
import Cargos from "./pages/Cargos.jsx";
import Departamentos from "./pages/Departamentos.jsx";
import Vinculos from "./pages/Vinculos.jsx";
import Usuarios from "./pages/Usuarios.jsx";

function AreaLogada() {
  return (
    <div className="app-shell">
      <Sidebar />
      <main className="content">
        <Routes>
          <Route path="/" element={<Navigate to="/funcionarios" replace />} />
          <Route path="/funcionarios" element={<Funcionarios />} />
          <Route path="/cargos" element={<Cargos />} />
          <Route path="/departamentos" element={<Departamentos />} />
          <Route path="/vinculos" element={<Vinculos />} />
          <Route
            path="/usuarios"
            element={
              <RotaProtegida apenasAdmin>
                <Usuarios />
              </RotaProtegida>
            }
          />
        </Routes>
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/*"
          element={
            <RotaProtegida>
              <AreaLogada />
            </RotaProtegida>
          }
        />
      </Routes>
    </AuthProvider>
  );
}
