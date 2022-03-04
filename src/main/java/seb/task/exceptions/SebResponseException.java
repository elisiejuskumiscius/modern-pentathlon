package seb.task.exceptions;

import seb.task.messages.Message;
import seb.task.messages.MessageBuilder;
import seb.task.messages.ValidationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SebResponseException extends SebException {
    private List<ValidationMessage> validationMessageList;

    public SebResponseException(SebExceptionStatus status,
                                List<ValidationMessage> validationMessageList) {
        super(status);
        this.validationMessageList = validationMessageList;
    }

    public SebResponseException() {
        super(SebExceptionStatus.FAIL, SebExceptionStatus.FAIL.name(), null);
    }

    public List<ValidationMessage> getValidationMessageList() {
        return validationMessageList;
    }

    public SebResponseException withValidationMessage(MessageBuilder messageBuilder) {
        if (validationMessageList == null || validationMessageList.isEmpty()) {
            validationMessageList = new ArrayList<>();
        }
        validationMessageList.add(messageBuilder.buildValidation());
        return this;
    }

    public static SebResponseException createValidation(Message errorMessage) {
        Objects.requireNonNull(errorMessage);
        return new SebResponseException().withValidationMessage(new MessageBuilder().text(errorMessage.getText())
                .code(errorMessage.getId().toString()));
    }

    public static SebResponseException createValidation(Message errorMessage, String object) {
        Objects.requireNonNull(errorMessage);
        return new SebResponseException().withValidationMessage(new MessageBuilder().text(errorMessage.getText())
                .code(errorMessage.getId().toString()).object(object));
    }

}
