export default function TabelaVinculos({ vinculos, onDesvincular }) {
  if (vinculos.length === 0) {
    return (
      <div className="table-card">
        <div className="empty-state">Nenhum vínculo cadastrado.</div>
      </div>
    );
  }

  return (
    <div className="table-card">
      <table className="data-table">
        <thead>
          <tr>
            <th>Funcionário</th>
            <th>Empresa</th>
            <th>Matrícula</th>
            <th>Cargo</th>
            <th>Departamento</th>
            <th className="col-icon">Vinculado</th>
          </tr>
        </thead>
        <tbody>
          {vinculos.map((v) => (
            <tr key={v.id}>
              <td>{v.funcionarioNome}</td>
              <td>{v.empresa}</td>
              <td>{v.matricula}</td>
              <td>{v.cargoCodigo}</td>
              <td>{v.departamentoNome}</td>
              <td>
                <button
                  type="button"
                  className="switch switch--on"
                  role="switch"
                  aria-checked="true"
                  aria-label={`Desvincular ${v.funcionarioNome} de ${v.empresa}`}
                  title="Clique para desvincular"
                  onClick={() => onDesvincular(v)}
                >
                  <span className="switch__thumb" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
