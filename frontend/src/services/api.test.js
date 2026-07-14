import api, { extrairMensagemErro } from "./api.js";

describe("extrairMensagemErro", () => {
  it("usa a mensagem enviada pelo backend", () => {
    const error = { response: { data: { mensagem: "CPF inválido." } } };
    expect(extrairMensagemErro(error, "padrão")).toBe("CPF inválido.");
  });

  it("usa a mensagem padrão quando o backend não envia mensagem", () => {
    expect(extrairMensagemErro({}, "padrão")).toBe("padrão");
    expect(extrairMensagemErro(null, "padrão")).toBe("padrão");
    expect(extrairMensagemErro({ response: { data: {} } }, "padrão")).toBe("padrão");
  });
});

describe("interceptor de request", () => {
  const onRequest = api.interceptors.request.handlers[0].fulfilled;

  afterEach(() => localStorage.clear());

  it("adiciona o header Authorization quando há token salvo", () => {
    localStorage.setItem("dixi_token", "abc123");
    const config = onRequest({ headers: {} });
    expect(config.headers.Authorization).toBe("Bearer abc123");
  });

  it("não adiciona o header sem token", () => {
    const config = onRequest({ headers: {} });
    expect(config.headers.Authorization).toBeUndefined();
  });
});

describe("interceptor de response", () => {
  const onSuccess = api.interceptors.response.handlers[0].fulfilled;
  const onError = api.interceptors.response.handlers[0].rejected;

  afterEach(() => localStorage.clear());

  it("repassa respostas de sucesso", () => {
    const response = { status: 200 };
    expect(onSuccess(response)).toBe(response);
  });

  // 401 significa sessão expirada: as credenciais salvas são descartadas
  it("em erro 401 limpa as credenciais salvas", async () => {
    localStorage.setItem("dixi_token", "abc123");
    localStorage.setItem("dixi_usuario", "admin");

    await expect(onError({ response: { status: 401 } })).rejects.toEqual({ response: { status: 401 } });

    expect(localStorage.getItem("dixi_token")).toBeNull();
    expect(localStorage.getItem("dixi_usuario")).toBeNull();
  });

  it("em outros erros mantém as credenciais", async () => {
    localStorage.setItem("dixi_token", "abc123");

    await expect(onError({ response: { status: 400 } })).rejects.toEqual({ response: { status: 400 } });

    expect(localStorage.getItem("dixi_token")).toBe("abc123");
  });
});
