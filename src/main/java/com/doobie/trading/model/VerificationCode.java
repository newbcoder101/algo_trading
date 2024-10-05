package com.doobie.trading.model;

import com.doobie.trading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @OneToOne
    private User user;

    private String emailId;

    private String mobileNo;

    private VerificationType verificationType;


}
