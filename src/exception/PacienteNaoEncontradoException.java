package exception;

public class PacienteNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	
	public PacienteNaoEncontradoException(String message) {
        super(message);
    }

    public PacienteNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacienteNaoEncontradoException(Throwable cause) {
        super(cause);
    }

}
