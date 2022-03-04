package seb.task.exceptions;

import lombok.Data;
import seb.task.messages.ValidationMessage;

import java.util.List;

@Data
public class ResponseBase {
    private ResponseStatus status;
    private List<ValidationMessage> messages;
}
