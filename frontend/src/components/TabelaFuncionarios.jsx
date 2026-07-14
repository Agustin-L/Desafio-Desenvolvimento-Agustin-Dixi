export default function TabelaFuncionarios({ funcionarios, onEditar, onVerVinculos }) {
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
          </tr>
        </thead>
        <tbody>
          {funcionarios.map((funcionario) => (
            <tr key={funcionario.id} className="clickable" onClick={() => onVerVinculos(funcionario)}>
              <td>
                <button
                  className="icon-btn"
                  onClick={(e) => {
                    e.stopPropagation();
                    onEditar(funcionario);
                  }}
                  aria-label="Editar funcionário"
                >
                  <span className="material-symbols-outlined">edit</span>
                </button>
              </td>
              <td>{funcionario.nome}</td>
              <td>{funcionario.cpf}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
