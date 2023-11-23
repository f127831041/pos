package com.soho.pos.ex;

import lombok.Data;

@Data
public class PortalException extends RuntimeException {
    private String message;
    private int code;

    public PortalException(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public PortalException(String message) {
        this.message = message;
        this.code = -1;
    }
}
