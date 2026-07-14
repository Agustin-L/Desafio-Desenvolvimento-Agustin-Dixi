export default function TabelaDepartamentos({ departamentos, onEditar }) {
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
          </tr>
        </thead>
        <tbody>
          {departamentos.map((departamento) => (
            <tr key={departamento.id}>
              <td>
                <button
                  className="icon-btn"
                  onClick={() => onEditar(departamento)}
                  aria-label="Editar departamento"
                >
                  <span className="material-symbols-outlined">edit</span>
                </button>
              </td>
              <td>{departamento.codigo}</td>
              <td>{departamento.descricao}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
