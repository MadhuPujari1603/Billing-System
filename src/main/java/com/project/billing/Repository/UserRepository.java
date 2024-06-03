package com.project.billing.Repository;

import com.project.billing.Dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByPhone(String phone);

    public  Optional <User> findByEmail(String email);

    public User findByOtp(int otp);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    public  User updateNewPassword(String password,long id);




}
