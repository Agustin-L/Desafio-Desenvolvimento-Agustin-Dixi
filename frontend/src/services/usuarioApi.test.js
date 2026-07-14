import api from "./api.js";
import usuarioApi from "./usuarioApi.js";

vi.mock("./api.js", () => ({
  default: { get: vi.fn(), post: vi.fn(), delete: vi.fn() },
}));

beforeEach(() => vi.clearAllMocks());

describe("usuarioApi", () => {
  it("listar busca todos os usuários", async () => {
    api.get.mockResolvedValue({ data: [{ id: 1, username: "admin" }] });

    expect(await usuarioApi.listar()).toEqual([{ id: 1, username: "admin" }]);
    expect(api.get).toHaveBeenCalledWith("/usuarios");
  });

  it("cadastrar envia POST com os dados", async () => {
    const dados = { username: "maria", senha: "123456", perfil: "PADRAO" };
    api.post.mockResolvedValue({ data: { id: 2 } });

    expect(await usuarioApi.cadastrar(dados)).toEqual({ id: 2 });
    expect(api.post).toHaveBeenCalledWith("/usuarios", dados);
  });

  it("excluir envia DELETE para o id", async () => {
    api.delete.mockResolvedValue({});

    await usuarioApi.excluir(2);

    expect(api.delete).toHaveBeenCalledWith("/usuarios/2");
  });
});
