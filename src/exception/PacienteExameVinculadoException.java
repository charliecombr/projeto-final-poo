package exception;

public class PacienteExameVinculadoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	
	public PacienteExameVinculadoException(String message) {
        super(message);
    }

    public PacienteExameVinculadoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacienteExameVinculadoException(Throwable cause) {
        super(cause);
    }
}