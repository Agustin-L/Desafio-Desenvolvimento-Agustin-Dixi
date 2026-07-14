import api from "./api.js";

const cargoApi = {
  listar: () => api.get("/cargos").then((res) => res.data),
  buscarPorId: (id) => api.get(`/cargos/${id}`).then((res) => res.data),
  cadastrar: (dados) => api.post("/cargos", dados).then((res) => res.data),
  editar: (id, dados) => api.put(`/cargos/${id}`, dados).then((res) => res.data),
  excluir: (id) => api.delete(`/cargos/${id}`),
};

export default cargoApi;
