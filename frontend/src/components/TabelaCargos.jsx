export default function TabelaCargos({ cargos, onEditar, onExcluir }) {
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
            {onExcluir && <th className="col-icon">Excluir</th>}
          </tr>
        </thead>
        <tbody>
          {cargos.map((cargo) => (
            <tr key={cargo.id}>
              <td>
                <button
                  className="icon-btn"
                  onClick={() => onEditar(cargo)}
                  aria-label={`Editar cargo ${cargo.codigo} — ${cargo.descricao}`}
                >
                  <span className="material-symbols-outlined" aria-hidden="true">edit</span>
                </button>
              </td>
              <td>{cargo.codigo}</td>
              <td>{cargo.descricao}</td>
              {onExcluir && (
                <td>
                  <button
                    className="icon-btn"
                    onClick={() => onExcluir(cargo)}
                    aria-label={`Excluir cargo ${cargo.codigo} — ${cargo.descricao}`}
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
