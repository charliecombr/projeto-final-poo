package exception;

public class ConfiguracaoInvalidaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	
	public ConfiguracaoInvalidaException(String message) {
        super(message);
    }

    public ConfiguracaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfiguracaoInvalidaException(Throwable cause) {
        super(cause);
    }

}

