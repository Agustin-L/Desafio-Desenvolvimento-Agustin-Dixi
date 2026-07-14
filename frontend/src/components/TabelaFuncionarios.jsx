import { formatarCpf } from "../utils/cpf.js";

export default function TabelaFuncionarios({ funcionarios, onEditar, onVerVinculos, onInativar, onReativar }) {
  const podeAlterarSituacao = Boolean(onInativar && onReativar);

  if (funcionarios.length === 0) {
    return (
      <div className="table-card">
        <div className="empty-state">Nenhum funcionário encontrado.</div>
      </div>
    );
  }

  return (
    <div className="table-card">
      <table className="data-table">
        <thead>
          <tr>
            <th className="col-icon">Editar</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Situação</th>
            {podeAlterarSituacao && <th className="col-icon">Ações</th>}
          </tr>
        </thead>
        <tbody>
          {funcionarios.map((funcionario) => {
            const inativo = funcionario.ativo === false;
            return (
              <tr
                key={funcionario.id}
                className={`clickable${inativo ? " linha-inativa" : ""}`}
                tabIndex={0}
                title="Ver vínculos do funcionário"
                onClick={() => onVerVinculos(funcionario)}
                onKeyDown={(e) => {
                  if (e.key === "Enter" || e.key === " ") {
                    e.preventDefault();
                    onVerVinculos(funcionario);
                  }
                }}
              >
                <td>
                  <button
                    className="icon-btn"
                    onClick={(e) => {
                      e.stopPropagation();
                      onEditar(funcionario);
                    }}
                    aria-label={`Editar funcionário ${funcionario.nome}`}
                  >
                    <span className="material-symbols-outlined" aria-hidden="true">edit</span>
                  </button>
                </td>
                <td>{funcionario.nome}</td>
                <td>{formatarCpf(funcionario.cpf)}</td>
                <td>
                  <span className={`badge ${inativo ? "badge--inativo" : "badge--ativo"}`}>
                    {inativo ? "Inativo" : "Ativo"}
                  </span>
                </td>
                {podeAlterarSituacao && (
                  <td>
                    {inativo ? (
                      <button
                        className="icon-btn"
                        title="Reativar funcionário"
                        onClick={(e) => {
                          e.stopPropagation();
                          onReativar(funcionario);
                        }}
                        aria-label={`Reativar funcionário ${funcionario.nome}`}
                      >
                        <span className="material-symbols-outlined" aria-hidden="true">person_check</span>
                      </button>
                    ) : (
                      <button
                        className="icon-btn"
                        title="Inativar funcionário"
                        onClick={(e) => {
                          e.stopPropagation();
                          onInativar(funcionario);
                        }}
                        aria-label={`Inativar funcionário ${funcionario.nome}`}
                      >
                        <span className="material-symbols-outlined" aria-hidden="true">person_off</span>
                      </button>
                    )}
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
