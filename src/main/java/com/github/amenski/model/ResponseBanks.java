package com.github.amenski.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseBanks extends Response {

    private List<Bank> data;

    public List<Bank> getData() {
        if(data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public ResponseBanks setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    public ResponseBanks setStatus(String status) {
        super.setStatus(status);
        return this;
    }

    public ResponseBanks setStatusCode(int statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }

    public ResponseBanks setRawJson(String rawJson) {
        super.setRawJson(rawJson);
        return this;
    }

    public ResponseBanks setData(List<Bank> data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseBanks{" + "status=" + this.getStatus() +
                ", statusCode=" + this.getStatusCode() +
                ", message=" + this.getMessage() +
                ", data=" + data +
                '}';
    }
}
