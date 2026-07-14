import api from "./api.js";
import authApi from "./authApi.js";

vi.mock("./api.js", () => ({
  default: { post: vi.fn() },
}));

describe("authApi", () => {
  it("login envia as credenciais e retorna os dados da sessão", async () => {
    const sessao = { token: "abc", username: "admin", perfil: "ADMIN" };
    api.post.mockResolvedValue({ data: sessao });

    expect(await authApi.login({ username: "admin", senha: "admin123" })).toEqual(sessao);
    expect(api.post).toHaveBeenCalledWith("/auth/login", { username: "admin", senha: "admin123" });
  });
});
