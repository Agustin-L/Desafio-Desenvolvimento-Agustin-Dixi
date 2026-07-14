import api from "./api.js";

const usuarioApi = {
  listar: () => api.get("/usuarios").then((res) => res.data),
  cadastrar: (dados) => api.post("/usuarios", dados).then((res) => res.data),
  excluir: (id) => api.delete(`/usuarios/${id}`),
};

export default usuarioApi;
