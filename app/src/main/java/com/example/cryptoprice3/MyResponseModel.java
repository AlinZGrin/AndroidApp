package com.example.cryptoprice3;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class MyResponseModel {

    @SerializedName("status")
    public Status status;
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public class Status {
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
    public Data data;
    public Data getData() {
        return data;
    }
    public void setData(Data data){
        this.data=data;
    }
    public class Data {
        @SerializedName("1")
        private One one;

        public One getOne() {
            return one;
        }
        public void setOne(One one){
            this.one=one;
        }

    }

    public class One {
        @SerializedName("name")
        private String name;
        @SerializedName("quote")
        private Quote quote;
        public String getName() {
            return name;
        }
        public void setQuote(String name){
            this.name=name;
        }
        public Quote getQuote() {
            return quote;
        }
        public void setQuote(Quote quote){
            this.quote=quote;
        }
    }

    public class Quote {
        @SerializedName("USD")
        private USD usd;

        public USD getUSD() {
            return usd;
        }
        public void setUSD(USD usd){
            this.usd=usd;
        }
    }
    public class USD {
        @SerializedName("price")
        private float price;

        public float getPrice() {
            return price;
        }
        public void setPrice(float price){
            this.price=price;
        }
    }
}


