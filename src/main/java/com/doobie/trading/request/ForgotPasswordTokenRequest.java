package com.doobie.trading.request;

import com.doobie.trading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest
{
    private  String sendTo;
    private VerificationType verificationType;

}
