package seb.task.messages;

public enum ErrorMessage implements Message {

    FAIL_READING_FILE(-0L, "Could not read file!"),
    NO_FIRST_RESULT(-1L, "Could not find first time result!");

    private final Long id;
    private final String text;

    ErrorMessage(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

}
