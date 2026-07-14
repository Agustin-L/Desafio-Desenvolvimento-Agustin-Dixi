import api from "./api.js";
import vinculoApi from "./vinculoApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn(), post: vi.fn(), put: vi.fn(), delete: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("vinculoApi", () => {
  it("listarPorFuncionario envia o funcionarioId como param", async () => {
    const dados = [{ id: 10 }];
    api.get.mockResolvedValue({ data: dados });

    expect(await vinculoApi.listarPorFuncionario(1)).toEqual(dados);
    expect(api.get).toHaveBeenCalledWith("/vinculos", { params: { funcionarioId: 1 } });
  });

  it("listarTodos usa a rota /vinculos/todos", async () => {
    api.get.mockResolvedValue({ data: [] });

    await vinculoApi.listarTodos();

    expect(api.get).toHaveBeenCalledWith("/vinculos/todos");
  });

  it("cadastrar envia POST com os dados", async () => {
    const dados = { empresa: "Dixi", matricula: "1000", funcionarioId: 1, cargoId: 2, departamentoId: 3 };
    api.post.mockResolvedValue({ data: { id: 10, ...dados } });

    expect(await vinculoApi.cadastrar(dados)).toEqual({ id: 10, ...dados });
    expect(api.post).toHaveBeenCalledWith("/vinculos", dados);
  });

  it("editar envia PUT para o id", async () => {
    const dados = { empresa: "Beta", matricula: "2000", funcionarioId: 1, cargoId: 2, departamentoId: 3 };
    api.put.mockResolvedValue({ data: { id: 10, ...dados } });

    expect(await vinculoApi.editar(10, dados)).toEqual({ id: 10, ...dados });
    expect(api.put).toHaveBeenCalledWith("/vinculos/10", dados);
  });

  it("excluir envia DELETE para o id", async () => {
    api.delete.mockResolvedValue({});

    await vinculoApi.excluir(10);

    expect(api.delete).toHaveBeenCalledWith("/vinculos/10");
  });
});
