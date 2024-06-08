package com.github.amenski.model;

import com.google.gson.annotations.SerializedName;

public class InitializeResponse extends Response {

    private Data data;

    public InitializeResponse() {
    }

    public InitializeResponse(String rawJson, String message, String status, int statusCode, Data data) {
        super(rawJson, message, status, statusCode);
        this.data = data;
    }

    public InitializeResponse setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    public InitializeResponse setStatus(String status) {
        super.setStatus(status);
        return this;
    }

    public InitializeResponse setStatusCode(int statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }

    public InitializeResponse setRawJson(String rawJson) {
        super.setRawJson(rawJson);
        return this;
    }

    public InitializeResponse setData(Data data) {
        this.data = data;
        return this;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("checkout_url")
        private String checkOutUrl;

        public String getCheckOutUrl() {
            return checkOutUrl;
        }

        public void setCheckOutUrl(String checkOutUrl) {
            this.checkOutUrl = checkOutUrl;
        }

        @Override
        public String toString() {
            return "Data {" + "checkOutUrl='" + checkOutUrl + "'}";
        }
    }

    @Override
    public String toString() {
        return "InitializeResponse{" + "status=" + this.getStatus() +
                ", statusCode=" + this.getStatusCode() +
                ", message=" + this.getMessage() +
                ", data=" + data +
                "}";
    }
}
