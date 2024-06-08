package com.github.amenski.model;

/**
 * The <code>Response</code> class is an abstract class that
 * represents the response data from Chapa API.
 */
public abstract class Response {

    private String message;
    private String status;
    private int statusCode;
    private String rawJson;

    public Response() {
    }

    public Response(String rawJson, String message, String status, int statusCode) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.rawJson = rawJson;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Response setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Response setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getRawJson() {
        return rawJson;
    }

    /**
     * An alias for {@link #getRawJson()}.
     * @return String representation of the response JSON data.
     */
    public String asString() {
        return rawJson;
    }

    public Response setRawJson(String rawJson) {
        this.rawJson = rawJson;
        return this;
    }
}
