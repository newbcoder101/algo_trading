package com.doobie.trading.service;

import com.doobie.trading.config.JwtProvider;
import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.TwoFactorAuth;
import com.doobie.trading.model.User;
import com.doobie.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;



    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String emailId= JwtProvider.getEmailFromToken(jwt);
        User user =userRepository.findByEmailId(emailId);
        if(user==null)throw new Exception("User Not Found!!");
        return user;
    }

    @Override
    public User findUserByEmailId(String emailId) throws Exception {

        User user= userRepository.findByEmailId(emailId);

        if(user==null)throw new Exception("User Not Found!!");
        return user;
    }

    @Override
    public User findUserById(Long id) throws Exception {
        Optional<User> user=userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new Exception("User Not Found !!!");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentocation(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public User updateNewPassword(User user, String newPassword) {
        User userFromDb=userRepository.findByEmailId(user.getEmailId());
        userFromDb.setPassword(newPassword);

        return userRepository.save(userFromDb);
    }
}
