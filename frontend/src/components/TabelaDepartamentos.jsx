export default function TabelaDepartamentos({ departamentos, onEditar, onExcluir }) {
  if (departamentos.length === 0) {
    return (
      <div className="table-card">
        <div className="empty-state">Nenhum departamento encontrado.</div>
      </div>
    );
  }

  return (
    <div className="table-card">
      <table className="data-table">
        <thead>
          <tr>
            <th className="col-icon">Editar</th>
            <th>Código</th>
            <th>Descrição</th>
            {onExcluir && <th className="col-icon">Excluir</th>}
          </tr>
        </thead>
        <tbody>
          {departamentos.map((departamento) => (
            <tr key={departamento.id}>
              <td>
                <button
                  className="icon-btn"
                  onClick={() => onEditar(departamento)}
                  aria-label={`Editar departamento ${departamento.codigo} — ${departamento.descricao}`}
                >
                  <span className="material-symbols-outlined" aria-hidden="true">edit</span>
                </button>
              </td>
              <td>{departamento.codigo}</td>
              <td>{departamento.descricao}</td>
              {onExcluir && (
                <td>
                  <button
                    className="icon-btn"
                    onClick={() => onExcluir(departamento)}
                    aria-label={`Excluir departamento ${departamento.codigo} — ${departamento.descricao}`}
                  >
                    <span className="material-symbols-outlined" aria-hidden="true">delete</span>
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
