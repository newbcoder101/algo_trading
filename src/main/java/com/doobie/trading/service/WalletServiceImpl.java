package com.doobie.trading.service;

import com.doobie.trading.domain.OrderStatus;
import com.doobie.trading.domain.OrderType;
import com.doobie.trading.model.Order;
import com.doobie.trading.model.User;
import com.doobie.trading.model.Wallet;
import com.doobie.trading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);

        }
        return wallet;

    }

    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {

        BigDecimal balance = wallet.getBalance();
        BigDecimal newbalance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newbalance);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);

        if (!wallet.isPresent()) {
            throw new Exception("Wallet for id not found !! ");
        }
        return wallet.get();

    }

    @Override
    public Wallet walletToWalletTranfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = walletRepository.findByUserId(sender.getId());
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient balance to send money");
        }
        senderWallet.setBalance(senderWallet.getBalance().subtract(BigDecimal.valueOf(amount)));
        receiverWallet.setBalance(receiverWallet.getBalance().add(BigDecimal.valueOf(amount)));
        walletRepository.save(receiverWallet);

        return walletRepository.save(senderWallet);
    }

    @Override
    public Wallet transferMoney(Wallet senderWallet, Wallet receiverWallet, Long amount) throws Exception {
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient balance!!");
        }
        senderWallet.setBalance(senderWallet.getBalance().subtract(BigDecimal.valueOf(amount)));
        receiverWallet.setBalance(receiverWallet.getBalance().add(BigDecimal.valueOf(amount)));
        walletRepository.save(receiverWallet);

        return walletRepository.save(senderWallet);
    }

    @Override
    public Wallet payOrder(Order order, User user) throws Exception {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (order.getOrderType().equals(OrderType.BUY)) {

            if (wallet.getBalance().compareTo(BigDecimal.valueOf(order.getPrice())) < 0) {
                throw new Exception("Insufficient Balanace");
            }
            wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(order.getPrice())));
            order.setOrderStatus(OrderStatus.SUCCEFULL);
        }
        else
        {
            wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(order.getPrice())));
            order.setOrderStatus(OrderStatus.SUCCEFULL);

        }
        walletRepository.save(wallet);

        return wallet;
    }
}
