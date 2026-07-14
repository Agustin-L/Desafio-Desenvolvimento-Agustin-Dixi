import api from "./api.js";

const funcionarioApi = {
  listar: (filtros = {}) => api.get("/funcionarios", { params: filtros }).then((res) => res.data),
  buscarPorId: (id) => api.get(`/funcionarios/${id}`).then((res) => res.data),
  cadastrar: (dados) => api.post("/funcionarios", dados).then((res) => res.data),
  editar: (id, dados) => api.put(`/funcionarios/${id}`, dados).then((res) => res.data),
  excluir: (id) => api.delete(`/funcionarios/${id}`),
};

export default funcionarioApi;
