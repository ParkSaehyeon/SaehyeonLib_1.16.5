package me.saehyeon.saehyeonlib.main;

import me.saehyeon.saehyeonlib.util.Playerf;

public enum ErrorMessage {
    NO_PERMISSION("권한이 없어요."),
    REGION_NOT_EXIST("등록되지 않은 지역이에요.");

    String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toMessage() {
        return errorMessage;
    }
}
