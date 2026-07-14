import api from "./api.js";

const authApi = {
  login: (dados) => api.post("/auth/login", dados).then((res) => res.data),
};

export default authApi;
