package seb.task.messages;

public class ValidationMessage {

    private String text;
    private String code;
    private String object;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public ValidationMessage code(String code) {
        this.code = code;
        return this;
    }

    public ValidationMessage text(String text) {
        this.text = text;
        return this;
    }

    public ValidationMessage object(String object) {
        this.object = object;
        return this;
    }

}
