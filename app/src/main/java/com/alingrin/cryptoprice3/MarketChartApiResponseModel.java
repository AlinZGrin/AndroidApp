package com.alingrin.cryptoprice3;



import com.google.gson.annotations.SerializedName;

        import java.util.List;

public class MarketChartApiResponseModel {
    @SerializedName("prices")
    private List<List<Double>> prices;

    @SerializedName("market_caps")
    private List<List<Double>> marketCaps;

    @SerializedName("total_volumes")
    private List<List<Double>> totalVolumes;

    // Getter methods for prices, marketCaps, and totalVolumes

    public List<List<Double>> getPrices() {
        return prices;
    }

    public List<List<Double>> getMarketCaps() {
        return marketCaps;
    }

    public List<List<Double>> getTotalVolumes() {
        return totalVolumes;
    }
}
