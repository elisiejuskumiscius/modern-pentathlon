package seb.task.messages;

public class MessageBuilder {

    private String text;
    private String code;
    private String object;

    public MessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder code(String code) {
        this.code = code;
        return this;
    }

    public MessageBuilder object(String object) {
        this.object = object;
        return this;
    }

    public MessageBuilder errorMessage(Message errorMessage) {
        code = errorMessage.getId().toString();
        text = errorMessage.getText();
        return this;
    }

    public ValidationMessage buildValidation() {
        return new ValidationMessage().code(code).text(text).object(object);
    }

}
