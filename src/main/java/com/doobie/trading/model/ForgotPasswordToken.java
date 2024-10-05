package com.doobie.trading.model;

import com.doobie.trading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Entity
@Data
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private  User user;

    private String otp;

    private VerificationType verificationType;

    private String  sendTo;
}
