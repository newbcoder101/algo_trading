package com.doobie.trading.service;

import com.doobie.trading.model.Order;
import com.doobie.trading.model.User;
import com.doobie.trading.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet,Long amount);
    Wallet findById(Long id) throws Exception;
    Wallet walletToWalletTranfer(User sender, Wallet reeiverWallet, Long amount) throws Exception;
    Wallet transferMoney(Wallet senderWallet,Wallet receiverWallet,Long amount) throws Exception;
    Wallet payOrder(Order order, User user) throws Exception;
}
