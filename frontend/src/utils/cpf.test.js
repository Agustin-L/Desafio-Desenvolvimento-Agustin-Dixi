import { limparCpf, formatarCpf, cpfValido } from "./cpf.js";

describe("limparCpf", () => {
  it("remove tudo que não é dígito", () => {
    expect(limparCpf("529.982.247-25")).toBe("52998224725");
    expect(limparCpf("abc529xyz")).toBe("529");
  });

  it("corta em 11 dígitos", () => {
    expect(limparCpf("529982247251234")).toBe("52998224725");
  });

  it("retorna vazio para null, undefined e vazio", () => {
    expect(limparCpf(null)).toBe("");
    expect(limparCpf(undefined)).toBe("");
    expect(limparCpf("")).toBe("");
  });
});

describe("formatarCpf", () => {
  it("aplica a máscara completa", () => {
    expect(formatarCpf("52998224725")).toBe("529.982.247-25");
  });

  it("mantém a máscara de um valor já formatado", () => {
    expect(formatarCpf("529.982.247-25")).toBe("529.982.247-25");
  });

  // a máscara vai aparecendo conforme o usuário digita
  it("aplica a máscara progressivamente", () => {
    expect(formatarCpf("529")).toBe("529");
    expect(formatarCpf("5299")).toBe("529.9");
    expect(formatarCpf("5299822")).toBe("529.982.2");
    expect(formatarCpf("5299822472")).toBe("529.982.247-2");
  });
});

describe("cpfValido", () => {
  it("aceita CPFs válidos com e sem máscara", () => {
    expect(cpfValido("529.982.247-25")).toBe(true);
    expect(cpfValido("52998224725")).toBe(true);
    expect(cpfValido("123.456.789-09")).toBe(true);
    expect(cpfValido("111.444.777-35")).toBe(true);
  });

  it("rejeita CPF com menos de 11 dígitos", () => {
    expect(cpfValido("5299822472")).toBe(false);
    expect(cpfValido("")).toBe(false);
    expect(cpfValido(null)).toBe(false);
  });

  it("rejeita CPF com todos os dígitos iguais", () => {
    expect(cpfValido("111.111.111-11")).toBe(false);
    expect(cpfValido("00000000000")).toBe(false);
  });

  it("rejeita CPF com dígito verificador errado", () => {
    expect(cpfValido("529.982.247-24")).toBe(false);
    expect(cpfValido("529.982.247-15")).toBe(false);
  });
});
