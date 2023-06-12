package it.aman.chapa.model;

import com.google.gson.annotations.SerializedName;

public class SubAccountResponseData extends ResponseData {

    private Data data;

    public SubAccountResponseData() {
    }

    public SubAccountResponseData(String rawJson, String message, String status, int statusCode, Data data) {
        super(rawJson, message, status, statusCode);
        this.data = data;
    }

    public SubAccountResponseData setMessage(String message) {
        return (SubAccountResponseData) super.setMessage(message);
    }

    public SubAccountResponseData setStatus(String status) {
        return (SubAccountResponseData) super.setStatus(status);
    }

    public SubAccountResponseData setStatusCode(int statusCode) {
        return (SubAccountResponseData) super.setStatusCode(statusCode);
    }

    public SubAccountResponseData setRawJson(String rawJson) {
        return (SubAccountResponseData) super.setRawJson(rawJson);
    }

    public SubAccountResponseData setData(Data data) {
        this.data = data;
        return this;
    }

    public Data getData() {
        return data;
    }

    private static class Data {
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
        final StringBuilder sb = new StringBuilder("SubAccountResponseData{");
        sb.append("status=").append(this.getStatus());
        sb.append(", statusCode=").append(this.getStatusCode());
        sb.append(", message=").append(this.getMessage());
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
