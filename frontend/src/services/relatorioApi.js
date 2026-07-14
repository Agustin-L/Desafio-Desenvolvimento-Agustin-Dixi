import api from "./api.js";

const relatorioApi = {
  obterDadosGerais: () => api.get("/relatorios/dados-gerais").then((res) => res.data),

  baixarRelatorioFuncionarios: async () => {
    const response = await api.get("/relatorios/funcionarios/csv", { responseType: "blob" });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "relatorio-funcionarios.csv");
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  },
};

export default relatorioApi;
