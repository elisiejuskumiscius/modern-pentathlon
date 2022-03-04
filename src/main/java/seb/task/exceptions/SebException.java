package seb.task.exceptions;

public class SebException extends Exception {

    private SebExceptionStatus status;

    public SebException(SebExceptionStatus status) {
        this.status = status;
    }

    public SebExceptionStatus getStatus() {
        return status;
    }

    public SebException(){
        super();
    }

    public SebException(SebExceptionStatus status, String message, Throwable cause){
        super(message, cause);
        this.status = status;
    }

}
