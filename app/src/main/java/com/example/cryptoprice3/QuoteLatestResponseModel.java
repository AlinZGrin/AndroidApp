package com.example.cryptoprice3;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Map;

public class QuoteLatestResponseModel {

    @SerializedName("status")
    public Status status;
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public static class Status {
        @SerializedName("timestamp")
        private Timestamp timestamp;

        public Timestamp getTimestamp() {
            return timestamp;
        }
        public void setStatus(Timestamp timestamp){
            this.timestamp=timestamp;
        }
    }

    @SerializedName("data")
    private Map<String, CryptoCurrency> data;
    public Map<String, CryptoCurrency> getData() {
        return data;
    }
    public void setData(Map<String, CryptoCurrency> data){
        this.data=data;
    }
    public static class CryptoCurrency {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name){
            this.name=name;
        }

    @SerializedName("quote")
    private Quote quote;
    public Quote getQuote() {
        return quote;
    }
    public void setQuote(Quote quote){
        this.quote=quote;
    }
}


public static class Quote {
    @SerializedName("USD")
    private USD usd;

    public USD getUSD() {
        return usd;
    }
    public void setUSD(USD usd){
        this.usd=usd;
    }
}
public static class USD {
    @SerializedName("price")
    private float price;
    public float getPrice() {
        return price;
    }
    public void setPrice(float price){
        this.price=price;
    }

    @SerializedName("percent_change_24h")
    private float percent_change_24h;

    public float getpercent_change_24h() {
        return percent_change_24h;
    }
    public void setpercent_change_24h(float percent_change_24h){
        this.percent_change_24h=percent_change_24h;
    }

}
}


