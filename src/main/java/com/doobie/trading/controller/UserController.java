package com.doobie.trading.controller;

import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.ForgotPasswordToken;
import com.doobie.trading.model.User;
import com.doobie.trading.model.VerificationCode;
import com.doobie.trading.request.ForgotPasswordTokenRequest;
import com.doobie.trading.request.ResetPasswordRequest;
import com.doobie.trading.response.ApiReponse;
import com.doobie.trading.response.AuthResponse;
import com.doobie.trading.service.EmailService;
import com.doobie.trading.service.ForgotPasswordService;
import com.doobie.trading.service.UserService;
import com.doobie.trading.service.VerificationCodeService;
import com.doobie.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private String jwt;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<User>sendVerificationOtp(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable VerificationType verificationType) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode=verificationCodeService.getVerifficationCodeByUser(user.getId());
        if(verificationCode==null)
        {
            verificationCode=verificationCodeService.sendVerificationCode(user,verificationType);
        }

        if(verificationType.equals(verificationType.EMAIL))
        {
            emailService.sendVerificaionOtpMail(user.getEmailId(),verificationCode.getOtp());
        }

        return  new ResponseEntity<User>(user,HttpStatus.OK);

    }


    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") @PathVariable String otp
                                                              ,String jwt) throws Exception {
      User user=userService.findUserProfileByJwt(jwt);

      VerificationCode verificationCode=verificationCodeService.getVerifficationCodeByUser(user.getId());

      String sendTo=verificationCode.getVerificationType()
              .equals(VerificationType.EMAIL)?verificationCode.getEmailId():verificationCode.getMobileNo();

      boolean isVerified=verificationCode.getOtp().equals(otp);

      if(isVerified)
      {
          User updatedUser=userService.enableTwoFactorAuthentocation(verificationCode.getVerificationType(),sendTo,user);
          return  new ResponseEntity<User>(updatedUser, HttpStatus.OK);
      }
      verificationCodeService.deleteVerificatonCode(verificationCode);
     throw new Exception("invalid otp !!!");

    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse>sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception {


        User user=userService.findUserByEmailId(req.getSendTo());
        String otp= OtpUtils.generateOtp();
        UUID uuid=UUID.randomUUID();
        String id=uuid.toString();

        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findByUser(user.getId());
        if(forgotPasswordToken==null)
        {
            forgotPasswordToken=forgotPasswordService.createToken(user,id,otp,req.getVerificationType(),req.getSendTo());
        }
        if(req.getVerificationType().equals(VerificationType.EMAIL))
        {
            emailService.sendVerificaionOtpMail(user.getEmailId(),forgotPasswordToken.getOtp());

        }
        AuthResponse response=new AuthResponse();
        response.setSession(forgotPasswordToken.getId());
        response.setMessage("Password Reset Otp sent successfully");

        return  new ResponseEntity<AuthResponse>(response,HttpStatus.OK);

    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiReponse> resetPassword(@RequestParam String id, @RequestBody ResetPasswordRequest request
            , String jwt) throws Exception {


        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);

        boolean isVerified=forgotPasswordToken.getOtp().equals(request.getOtp());
         if(isVerified)
         {
             userService.updateNewPassword(forgotPasswordToken.getUser(),request.getPassword());

        ApiReponse reponse=new ApiReponse();
         reponse.setMessage("password updated successfully!!");

         return new ResponseEntity<>(reponse,HttpStatus.OK);
         }
         throw new Exception("wrong otp");

    }


}
