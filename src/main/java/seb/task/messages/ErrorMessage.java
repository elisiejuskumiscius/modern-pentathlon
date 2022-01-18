package seb.task.messages;

public enum ErrorMessage implements Message {

    FAIL_READING_FILE(-0L, "Could not read file!"),
    NO_FIRST_RESULT(-1L, "Could not find first time result!"),
    PARSE_ERROR(-2L, "Could not parse athletes results"),
    FAIL_COUNTING_LINES(-3L, "Could not count file's lines");

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
