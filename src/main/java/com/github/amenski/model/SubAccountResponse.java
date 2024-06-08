package com.github.amenski.model;

import com.google.gson.annotations.SerializedName;

public class SubAccountResponse extends Response {

    private Data data;

    public SubAccountResponse() {
    }

    public SubAccountResponse(String rawJson, String message, String status, int statusCode, Data data) {
        super(rawJson, message, status, statusCode);
        this.data = data;
    }

    public SubAccountResponse setData(Data data) {
        this.data = data;
        return this;
    }

    public SubAccountResponse setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    public SubAccountResponse setStatus(String status) {
        super.setStatus(status);
        return this;
    }

    public SubAccountResponse setStatusCode(int statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }

    public SubAccountResponse setRawJson(String rawJson) {
        super.setRawJson(rawJson);
        return this;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
       @SerializedName("subaccounts[id]")
       private String subAccountId;

       public String getSubAccountId() {
           return subAccountId;
       }

         public Data setSubAccountId(String subAccountId) {
              this.subAccountId = subAccountId;
              return this;
         }
   }

    @Override
    public String toString() {
        return "SubAccountResponse{" + "status=" + this.getStatus() +
                ", statusCode=" + this.getStatusCode() +
                ", message=" + this.getMessage() +
                ", data=" + data +
                '}';
    }
}
