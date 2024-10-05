package com.doobie.trading.service;

import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.User;
import com.doobie.trading.model.VerificationCode;
import com.doobie.trading.repository.VerificationCodeRepository;
import com.doobie.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {


    @Autowired
    VerificationCodeRepository verificationCodeRepository;


    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCodeTosend=new VerificationCode();
        verificationCodeTosend.setUser(user);
        verificationCodeTosend.setOtp(OtpUtils.generateOtp());
        verificationCodeTosend.setVerificationType(verificationType);

        return verificationCodeRepository.save(verificationCodeTosend);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode>verificationCode=verificationCodeRepository.findById(id);
        if(verificationCode.isPresent())
        {
            return verificationCode.get();
        }
        throw new Exception("Verification code not found");
    }

    @Override
    public VerificationCode getVerifficationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificatonCode(VerificationCode verificationCode) {

        verificationCodeRepository.delete(verificationCode);
    }
}
