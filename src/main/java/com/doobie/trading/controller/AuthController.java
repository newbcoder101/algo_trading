package com.doobie.trading.controller;

import com.doobie.trading.config.JwtProvider;
import com.doobie.trading.model.TwoFactorOtp;
import com.doobie.trading.model.User;
import com.doobie.trading.repository.UserRepository;
import com.doobie.trading.response.AuthResponse;
import com.doobie.trading.service.CustomUserDetailService;
import com.doobie.trading.service.EmailService;
import com.doobie.trading.service.TwoFactorOtpService;
import com.doobie.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TwoFactorOtpService twoFactorOtpService;

    @Autowired
    EmailService emailService;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {


        User isEmailExist=userRepository.findByEmailId(user.getEmailId());

        if(isEmailExist!=null)
        {
            throw new Exception("Email id already in use by another user");
        }
        User newUser=new User();
        newUser.setEmailId(user.getEmailId());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());

        User savedUser=userRepository.save(newUser);

        Authentication auth=new UsernamePasswordAuthenticationToken(
                user.getEmailId(),
                user.getPassword()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
       String jwt= JwtProvider.generateToken(auth);
       AuthResponse authResponse=new AuthResponse();
       authResponse.setJwt(jwt);
       authResponse.setMessage("Registration successfull!!");
       authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

       String userName=user.getEmailId();

        String password=user.getPassword();

        Authentication auth=authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt= JwtProvider.generateToken(auth);

        User user1=userRepository.findByEmailId(user.getEmailId());
        if(user.getTwoFactorAuth().isEnabled())
        {
            AuthResponse response=new AuthResponse();
            response.setMessage("Two Factor Auth is Enabled");
            response.setTwoFactorAuthEnabled(true);
            String otp= OtpUtils.generateOtp();

            TwoFactorOtp oldTwoFactorOtp=twoFactorOtpService.findByUser(user1.getId());
            if(oldTwoFactorOtp!=null)
            {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOtp twoFactorOtp=twoFactorOtpService.createTwoFactorOtp(user1,otp,jwt);

            emailService.sendVerificaionOtpMail(user1.getEmailId(),otp);

            response.setSession(new TwoFactorOtp().getId());
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);

        }
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successfull!!");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String userName, String password) {

        UserDetails userDetails=customUserDetailService.loadUserByUsername(userName);
        String encodedPassword = passwordEncoder().encode(userDetails.getPassword());
        if(userDetails==null)
        {
            throw new BadCredentialsException("Invalid Username");
        }
        if(passwordEncoder().matches(password, userDetails.getPassword()))
        {
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping("two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp,@RequestParam  String id) throws Exception {

        TwoFactorOtp twoFactorOtp=twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp,otp))
        {
          AuthResponse authResponse=new AuthResponse();
          authResponse.setMessage("Otp Verified");
          authResponse.setTwoFactorAuthEnabled(true);
          authResponse.setJwt(twoFactorOtp.getJwt());
          return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }

       throw new Exception("Invalid Otp!!");

    }


}
