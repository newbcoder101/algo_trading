package com.doobie.trading.service;

import com.doobie.trading.model.TwoFactorOtp;
import com.doobie.trading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(User user,String otp ,String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp,String otp);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);

}
