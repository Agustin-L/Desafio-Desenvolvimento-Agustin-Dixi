import api from "./api.js";

const vinculoApi = {
  listarPorFuncionario: (funcionarioId) =>
    api.get("/vinculos", { params: { funcionarioId } }).then((res) => res.data),
  listarTodos: () => api.get("/vinculos/todos").then((res) => res.data),
  cadastrar: (dados) => api.post("/vinculos", dados).then((res) => res.data),
  editar: (id, dados) => api.put(`/vinculos/${id}`, dados).then((res) => res.data),
  excluir: (id) => api.delete(`/vinculos/${id}`),
};

export default vinculoApi;
