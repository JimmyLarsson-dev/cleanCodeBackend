package com.example.cleancode.payment.klarna;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KlarnaOrderDTO {

    @JsonProperty("purchase_country")
    private String purchase_country;

    @JsonProperty("purchase_currency")
    private String purchase_currency;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("order_amount")
    private int order_amount;

    @JsonProperty("order_tax_amount")
    private int order_tax_amount;

    @JsonProperty("order_lines")
    private List<OrderLine> orderLines;

    @Data
    public static class OrderLine {
        @JsonProperty("name")
        private String name;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("unit_price")
        private int unit_price;

        @JsonProperty("tax_rate")
        private int tax_rate;

        @JsonProperty("total_amount")
        private int total_amount;

        @JsonProperty("total_discount_amount")
        private int total_discount_amount;

        @JsonProperty("total_tax_amount")
        private int total_tax_amount;
    }

    @JsonProperty("merchant_urls")
    private MerchantUrls merchantUrls;

    @Data
    public static class MerchantUrls {
        @JsonProperty("terms")
        private String terms;

        @JsonProperty("checkout")
        private String checkout;

        @JsonProperty("confirmation")
        private String confirmation;

        @JsonProperty("push")
        private String push;
    }
}

