export function limparCpf(valor) {
  return (valor || "").replace(/\D/g, "").slice(0, 11);
}

export function formatarCpf(valor) {
  const digitos = limparCpf(valor);

  return digitos
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

export function cpfValido(valor) {
  const cpf = limparCpf(valor);

  if (cpf.length !== 11) return false;
  if (/^(\d)\1{10}$/.test(cpf)) return false;

  const calcularDigito = (fatorInicial, tamanho) => {
    let soma = 0;
    for (let i = 0; i < tamanho; i++) {
      soma += Number(cpf[i]) * (fatorInicial - i);
    }
    const resto = (soma * 10) % 11;
    return resto === 10 ? 0 : resto;
  };

  const digito1 = calcularDigito(10, 9);
  if (digito1 !== Number(cpf[9])) return false;

  const digito2 = calcularDigito(11, 10);
  if (digito2 !== Number(cpf[10])) return false;

  return true;
}
