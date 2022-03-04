package seb.task.exceptions;

public enum ResponseStatus {

    OK("Ok"),
    FAIL("Fail"),
    NOPERMISSION("NoPermission"),
    LOGINFAILED("LoginFailed"),
    NODATA("NoData"),
    BUSINESSFAILED("BusinessFailed"),
    NOTIMPLEMENTED("NotImplemented");

    private String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
