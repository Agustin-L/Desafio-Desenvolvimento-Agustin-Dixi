import api from "./api.js";
import relatorioApi from "./relatorioApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("relatorioApi", () => {
  it("obterDadosGerais busca os totais", async () => {
    api.get.mockResolvedValue({ data: { totalFuncionarios: 5 } });

    expect(await relatorioApi.obterDadosGerais()).toEqual({ totalFuncionarios: 5 });
    expect(api.get).toHaveBeenCalledWith("/relatorios/dados-gerais");
  });

  // o download é feito criando um link temporário com o blob do CSV
  it("baixarRelatorioFuncionarios baixa o CSV e dispara o download", async () => {
    api.get.mockResolvedValue({ data: "nome;cpf" });
    window.URL.createObjectURL = vi.fn(() => "blob:mock");
    window.URL.revokeObjectURL = vi.fn();
    const click = vi.spyOn(HTMLAnchorElement.prototype, "click").mockImplementation(() => {});
    const appendChild = vi.spyOn(document.body, "appendChild");

    await relatorioApi.baixarRelatorioFuncionarios();

    expect(api.get).toHaveBeenCalledWith("/relatorios/funcionarios/csv", { responseType: "blob" });
    const link = appendChild.mock.calls[0][0];
    expect(link.getAttribute("download")).toBe("relatorio-funcionarios.csv");
    expect(click).toHaveBeenCalledTimes(1);
    expect(window.URL.revokeObjectURL).toHaveBeenCalledWith("blob:mock");
  });
});
