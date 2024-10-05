package com.doobie.trading.service;

import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.User;
import com.doobie.trading.model.VerificationCode;
import org.springframework.stereotype.Service;


public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id ) throws Exception;

    VerificationCode getVerifficationCodeByUser(Long userId);

    void deleteVerificatonCode(VerificationCode verificationCode);




}
