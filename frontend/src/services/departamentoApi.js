import api from "./api.js";

const departamentoApi = {
  listar: () => api.get("/departamentos").then((res) => res.data),
  buscarPorId: (id) => api.get(`/departamentos/${id}`).then((res) => res.data),
  cadastrar: (dados) => api.post("/departamentos", dados).then((res) => res.data),
  editar: (id, dados) => api.put(`/departamentos/${id}`, dados).then((res) => res.data),
  excluir: (id) => api.delete(`/departamentos/${id}`),
};

export default departamentoApi;
