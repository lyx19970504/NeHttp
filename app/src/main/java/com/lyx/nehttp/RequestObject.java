package com.lyx.nehttp;

public class RequestObject {

    private String resultcode;
    private String reason;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", error_code=" + error_code +
                '}';
    }
}
