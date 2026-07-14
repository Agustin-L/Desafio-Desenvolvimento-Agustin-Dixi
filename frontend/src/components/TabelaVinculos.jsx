export default function TabelaVinculos({ vinculos, cargos = [], onDesvincular }) {
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
          {vinculos.map((v) => {
            const cargoDescricao = cargos.find((c) => String(c.id) === String(v.cargoId))?.descricao;
            return (
            <tr key={v.id}>
              <td>{v.funcionarioNome}</td>
              <td>{v.empresa}</td>
              <td>{v.matricula}</td>
              <td>{cargoDescricao ? `${v.cargoCodigo} — ${cargoDescricao}` : v.cargoCodigo}</td>
              <td>{v.departamentoNome}</td>
              <td>
                <button
                  type="button"
                  className="switch switch--on"
                  role="switch"
                  aria-checked="true"
                  aria-label={`Desvincular ${v.funcionarioNome} de ${v.empresa}`}
                  title={onDesvincular ? "Clique para desvincular" : "Apenas administradores podem desvincular"}
                  onClick={onDesvincular ? () => onDesvincular(v) : undefined}
                  disabled={!onDesvincular}
                >
                  <span className="switch__thumb" />
                </button>
              </td>
            </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
