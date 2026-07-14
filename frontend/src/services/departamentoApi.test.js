import api from "./api.js";
import departamentoApi from "./departamentoApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn(), post: vi.fn(), put: vi.fn(), delete: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("departamentoApi", () => {
  it("listar busca todos os departamentos", async () => {
    const dados = [{ id: 1, codigo: "D1" }];
    api.get.mockResolvedValue({ data: dados });

    expect(await departamentoApi.listar()).toEqual(dados);
    expect(api.get).toHaveBeenCalledWith("/departamentos");
  });

  it("buscarPorId busca pelo id", async () => {
    api.get.mockResolvedValue({ data: { id: 1 } });

    expect(await departamentoApi.buscarPorId(1)).toEqual({ id: 1 });
    expect(api.get).toHaveBeenCalledWith("/departamentos/1");
  });

  it("cadastrar envia POST com os dados", async () => {
    const dados = { codigo: "D1", descricao: "TI" };
    api.post.mockResolvedValue({ data: { id: 1, ...dados } });

    expect(await departamentoApi.cadastrar(dados)).toEqual({ id: 1, ...dados });
    expect(api.post).toHaveBeenCalledWith("/departamentos", dados);
  });

  it("editar envia PUT para o id", async () => {
    const dados = { codigo: "D1", descricao: "Tecnologia" };
    api.put.mockResolvedValue({ data: { id: 1, ...dados } });

    expect(await departamentoApi.editar(1, dados)).toEqual({ id: 1, ...dados });
    expect(api.put).toHaveBeenCalledWith("/departamentos/1", dados);
  });

  it("excluir envia DELETE para o id", async () => {
    api.delete.mockResolvedValue({});

    await departamentoApi.excluir(1);

    expect(api.delete).toHaveBeenCalledWith("/departamentos/1");
  });
});
