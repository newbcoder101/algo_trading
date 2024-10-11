package com.doobie.trading.service;

import com.doobie.trading.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page);

    String getMarketChart(String coinId,int days);

    String getCoinDetails(String coinId);

    Coin findById(String coinId) throws Exception;

    String searchCoin(String keyword);

    String getTop50CoinsByMarketCapRank();

    String getTrendingCoins();

}
