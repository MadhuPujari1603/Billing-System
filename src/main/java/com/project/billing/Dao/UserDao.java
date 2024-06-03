package com.project.billing.Dao;

import com.project.billing.Dto.User;

import com.project.billing.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {

    @Autowired
    public UserRepository userRepository;


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findById(long id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElse(null);
    }

    public User findByPhone(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        return user.orElse(null);
    }

    public Page<User> findAllUsers(int offset, int pageSize, String field) {
        return userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field).descending()));
    }

    public User findByEmail(String email) {
        Optional<User> user=  userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public User updateNewPassword(String password, long id) {
        return userRepository.updateNewPassword(password, id);
    }

    public User findByOtp(int otp) {
        return userRepository.findByOtp(otp);
    }

    public void deletUser(User user) {
        userRepository.delete(user);
    }

}
