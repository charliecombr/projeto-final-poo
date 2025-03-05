package exception;

public class ExameNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	
	public ExameNaoEncontradoException(String message) {
        super(message);
    }

    public ExameNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExameNaoEncontradoException(Throwable cause) {
        super(cause);
    }

}
