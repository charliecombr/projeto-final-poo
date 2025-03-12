package exception;

public class ValidarCpfException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    
    public ValidarCpfException(String message) {
        super(message);
    }

    public ValidarCpfException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidarCpfException(Throwable cause) {
        super(cause);
    }
}
