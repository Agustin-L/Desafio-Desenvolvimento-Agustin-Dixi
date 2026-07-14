import { useEffect } from "react";

export default function Modal({ title, onClose, children, footer, wide = false }) {
  useEffect(() => {
    const fecharComEsc = (e) => e.key === "Escape" && onClose();
    document.addEventListener("keydown", fecharComEsc);
    return () => document.removeEventListener("keydown", fecharComEsc);
  }, [onClose]);

  return (
    <div className="modal-overlay" onMouseDown={(e) => e.target === e.currentTarget && onClose()}>
      <div
        className={"modal-card" + (wide ? " modal-card--wide" : "")}
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-titulo"
      >
        <div className="modal-header">
          <h2 id="modal-titulo">{title}</h2>
          <button className="modal-close" onClick={onClose} aria-label="Fechar">
            <span className="material-symbols-outlined" aria-hidden="true">close</span>
          </button>
        </div>
        <div className="modal-body">{children}</div>
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  );
}
