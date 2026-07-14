import api from "./api.js";

const funcionarioApi = {
  listar: (filtros = {}) => api.get("/funcionarios", { params: filtros }).then((res) => res.data),
  buscarPorId: (id) => api.get(`/funcionarios/${id}`).then((res) => res.data),
  cadastrar: (dados) => api.post("/funcionarios", dados).then((res) => res.data),
  editar: (id, dados) => api.put(`/funcionarios/${id}`, dados).then((res) => res.data),
  // Regra CLT: funcionário não é excluído, apenas inativado/reativado
  inativar: (id) => api.patch(`/funcionarios/${id}/inativar`).then((res) => res.data),
  reativar: (id) => api.patch(`/funcionarios/${id}/reativar`).then((res) => res.data),
};

export default funcionarioApi;
