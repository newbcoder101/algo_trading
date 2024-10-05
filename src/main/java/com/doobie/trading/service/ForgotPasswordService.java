package com.doobie.trading.service;

import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.ForgotPasswordToken;
import com.doobie.trading.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp,
                                    VerificationType verificationType,String sendTo);
    ForgotPasswordToken findById( String id);
    ForgotPasswordToken findByUser(Long userId);
    void deleteToken(ForgotPasswordToken token);
}
