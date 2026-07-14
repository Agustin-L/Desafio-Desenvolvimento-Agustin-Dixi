export default function TabelaCargos({ cargos, onEditar }) {
  if (cargos.length === 0) {
    return (
      <div className="table-card">
        <div className="empty-state">Nenhum cargo encontrado.</div>
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
          {cargos.map((cargo) => (
            <tr key={cargo.id}>
              <td>
                <button className="icon-btn" onClick={() => onEditar(cargo)} aria-label="Editar cargo">
                  <span className="material-symbols-outlined">edit</span>
                </button>
              </td>
              <td>{cargo.codigo}</td>
              <td>{cargo.descricao}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
