package com.doobie.trading.service;

import com.doobie.trading.domain.VerificationType;
import com.doobie.trading.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserByEmailId(String emilId) throws Exception;
    public User findUserById(Long id) throws Exception;

    public User enableTwoFactorAuthentocation(VerificationType verificationType,String sendTo,User user);
    public User updateNewPassword(User user,String newPassword);
}
