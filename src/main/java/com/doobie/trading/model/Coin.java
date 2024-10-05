package com.doobie.trading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Coin {

    @Id
    private String id;
    private String symbol;
    private String name;
    private String image;
    private double currentPrice;
    private long marketCap;
    private int marketCapRank;
    private long fullyDilutedValuation;
    private long totalVolume;
    private double high24h;
    private double low24h;
    private double priceChange24h;
    private double priceChangePercentage24h;
    private long marketCapChange24h;
    private double marketCapChangePercentage24h;
    private long circulatingSupply;
    private long totalSupply;
    private long maxSupply;
    private double ath; // All-time high
    private double athChangePercentage;
    private LocalDateTime athDate;
    private double atl; // All-time low
    private double atlChangePercentage;
    private LocalDateTime atlDate;
    private Double roi;
    private LocalDateTime lastUpdated;
}
