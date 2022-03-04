package seb.task.exceptions;

@FunctionalInterface
public interface ExecutableControllerCode {
    void run() throws SebResponseException;
}
