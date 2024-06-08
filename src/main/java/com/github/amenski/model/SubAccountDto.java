package com.github.amenski.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import static com.github.amenski.utility.Util.putIfNotNull;

public class SubAccountDto {

    @SerializedName("business_name")
    private String businessName;
    @SerializedName("bank_code")
    private String bankCode;
    @SerializedName("account_name")
    private String accountName;
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("split_type")
    private SplitTypeEnum splitType;
    @SerializedName("split_value")
    private Double splitValue;

    public String getBusinessName() {
        return businessName;
    }

    public SubAccountDto setBusinessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    public String getBankCode() {
        return bankCode;
    }

    public SubAccountDto setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public SubAccountDto setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public SubAccountDto setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public SplitTypeEnum getSplitType() {
        return splitType;
    }

    public SubAccountDto setSplitType(SplitTypeEnum splitType) {
        this.splitType = splitType;
        return this;
    }

    public Double getSplitValue() {
        return splitValue;
    }

    public SubAccountDto setSplitValue(Double splitValue) {
        this.splitValue = splitValue;
        return this;
    }

    public Map<String, Object> getAsMap() {
        Map<String, Object> account = new HashMap<>();
        putIfNotNull(account, "business_name",  businessName);
        putIfNotNull(account, "account_name",   accountName);
        putIfNotNull(account, "account_number", accountNumber);
        putIfNotNull(account, "bank_code",      bankCode);
        putIfNotNull(account, "split_type",     splitType.name().toLowerCase());
        putIfNotNull(account, "split_value",    splitValue != null ? splitValue.toString() : null);
        return account;
    }
}
