import api from "./api.js";
import funcionarioApi from "./funcionarioApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn(), post: vi.fn(), put: vi.fn(), patch: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("funcionarioApi", () => {
  // os filtros de pesquisa viajam como query params
  it("listar envia os filtros como params e retorna os dados", async () => {
    const dados = [{ id: 1, nome: "Ana" }];
    api.get.mockResolvedValue({ data: dados });

    const resultado = await funcionarioApi.listar({ nome: "Ana", cargoId: 2 });

    expect(api.get).toHaveBeenCalledWith("/funcionarios", { params: { nome: "Ana", cargoId: 2 } });
    expect(resultado).toEqual(dados);
  });

  it("listar sem argumentos envia params vazios", async () => {
    api.get.mockResolvedValue({ data: [] });

    await funcionarioApi.listar();

    expect(api.get).toHaveBeenCalledWith("/funcionarios", { params: {} });
  });

  it("buscarPorId busca pelo id", async () => {
    api.get.mockResolvedValue({ data: { id: 7 } });

    const resultado = await funcionarioApi.buscarPorId(7);

    expect(api.get).toHaveBeenCalledWith("/funcionarios/7");
    expect(resultado).toEqual({ id: 7 });
  });

  it("cadastrar envia POST com os dados", async () => {
    const dados = { nome: "Ana", cpf: "529.982.247-25" };
    api.post.mockResolvedValue({ data: { id: 1, ...dados } });

    const resultado = await funcionarioApi.cadastrar(dados);

    expect(api.post).toHaveBeenCalledWith("/funcionarios", dados);
    expect(resultado.id).toBe(1);
  });

  it("editar envia PUT para o id", async () => {
    const dados = { nome: "Ana Paula", cpf: "529.982.247-25" };
    api.put.mockResolvedValue({ data: { id: 7, ...dados } });

    const resultado = await funcionarioApi.editar(7, dados);

    expect(api.put).toHaveBeenCalledWith("/funcionarios/7", dados);
    expect(resultado.nome).toBe("Ana Paula");
  });

  // regra CLT: funcionário não é excluído, apenas inativado/reativado
  it("inativar envia PATCH para a rota de inativação", async () => {
    api.patch.mockResolvedValue({ data: { id: 7, ativo: false } });

    const resultado = await funcionarioApi.inativar(7);

    expect(api.patch).toHaveBeenCalledWith("/funcionarios/7/inativar");
    expect(resultado.ativo).toBe(false);
  });

  it("reativar envia PATCH para a rota de reativação", async () => {
    api.patch.mockResolvedValue({ data: { id: 7, ativo: true } });

    const resultado = await funcionarioApi.reativar(7);

    expect(api.patch).toHaveBeenCalledWith("/funcionarios/7/reativar");
    expect(resultado.ativo).toBe(true);
  });
});
