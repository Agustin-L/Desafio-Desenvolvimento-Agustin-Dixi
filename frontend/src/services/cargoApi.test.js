import api from "./api.js";
import cargoApi from "./cargoApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn(), post: vi.fn(), put: vi.fn(), delete: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("cargoApi", () => {
  it("listar busca todos os cargos", async () => {
    const dados = [{ id: 1, codigo: "001" }];
    api.get.mockResolvedValue({ data: dados });

    expect(await cargoApi.listar()).toEqual(dados);
    expect(api.get).toHaveBeenCalledWith("/cargos");
  });

  it("buscarPorId busca pelo id", async () => {
    api.get.mockResolvedValue({ data: { id: 1 } });

    expect(await cargoApi.buscarPorId(1)).toEqual({ id: 1 });
    expect(api.get).toHaveBeenCalledWith("/cargos/1");
  });

  it("cadastrar envia POST com os dados", async () => {
    const dados = { codigo: "001", descricao: "Analista" };
    api.post.mockResolvedValue({ data: { id: 1, ...dados } });

    expect(await cargoApi.cadastrar(dados)).toEqual({ id: 1, ...dados });
    expect(api.post).toHaveBeenCalledWith("/cargos", dados);
  });

  it("editar envia PUT para o id", async () => {
    const dados = { codigo: "001", descricao: "Analista Sênior" };
    api.put.mockResolvedValue({ data: { id: 1, ...dados } });

    expect(await cargoApi.editar(1, dados)).toEqual({ id: 1, ...dados });
    expect(api.put).toHaveBeenCalledWith("/cargos/1", dados);
  });

  it("excluir envia DELETE para o id", async () => {
    api.delete.mockResolvedValue({});

    await cargoApi.excluir(1);

    expect(api.delete).toHaveBeenCalledWith("/cargos/1");
  });
});
