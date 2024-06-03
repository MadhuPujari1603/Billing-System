package com.project.billing.Service;

import com.project.billing.Dao.UserDao;
import com.project.billing.Dto.ResponseStructure;
import com.project.billing.Dto.User;
import com.project.billing.Util.EmailSenderService;
import com.project.billing.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserDao userdao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService javaMailSender;


    public ResponseEntity<ResponseStructure<User>> save(User user) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        if (userdao.findByEmail(user.getEmail()) != null || userdao.findByPhone(user.getPhone()) != null) {
            responseStructure.setStatus(HttpStatus.CONFLICT.value());
            responseStructure.setMessage("User Could Not Be Saved, Already Exists");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.CONFLICT);
        } else {
            responseStructure.setStatus(HttpStatus.CREATED.value());
            responseStructure.setMessage("User Saved Successfully");
            user.setRole("ADMIN");
            responseStructure.setData(userdao.saveUser(user));
            return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
        }
    }
    public ResponseEntity<ResponseStructure<User>> findByPhone(String phone) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findByPhone(phone);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User Does Not Exists To Be Found By ID ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User Found By ID = " + phone);
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> findById(long id)
    {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findById(id);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User Does Not Exists To Be Found By ID ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User Found By ID = " + id);
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }


    public ResponseEntity<ResponseStructure<Page<User>>> findAll(int offset, int pageSize, String field) {
        ResponseStructure<Page<User>> responseStructure = new ResponseStructure<>();
        Page<User> usersPage = userdao.findAllUsers(offset, pageSize, field);
        if (!usersPage.isEmpty()) {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("All Users found");
            responseStructure.setData(usersPage);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("No Users Found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure<User>> updateUser(User user) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user1 = userdao.findById(user.getId());
        if (Objects.isNull(user1)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User Not Found to Update ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User Updated " + user);
            responseStructure.setData(userdao.saveUser(user));
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);

        }
    }

    public ResponseEntity<ResponseStructure<User>> findByEmail(String  email) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findByEmail(email);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User Does Not Exists To Be Found By ID ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User Found By ID = " + email);
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> login(String email, String password) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findByEmail(email);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found with username/Email: " + email);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            if (password != null && user.getPassword().equals(password)) {
                responseStructure.setStatus(HttpStatus.OK.value());
                responseStructure.setMessage("Login successful for user: " + email);
                responseStructure.setData(user);
                return new ResponseEntity<>(responseStructure, HttpStatus.OK);
            } else {
                if (password == null) {
                    responseStructure.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    responseStructure.setMessage("Password not set for user: " + email);
                    return new ResponseEntity<>(responseStructure, HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    responseStructure.setStatus(HttpStatus.UNAUTHORIZED.value());
                    responseStructure.setMessage("Incorrect password for user: " + email);
                    return new ResponseEntity<>(responseStructure, HttpStatus.UNAUTHORIZED);
                }
            }
        }
    }

    public ResponseEntity<ResponseStructure<String>> forgotPassword(String email) {
        ResponseStructure<String> responseStructure = new ResponseStructure<>();
        User user = userdao.findByEmail(email);
        if (Objects.isNull(user)) {
            responseStructure.setStatus((HttpStatus.NOT_FOUND.value()));
            responseStructure.setMessage("Email Does Not Exist " + email);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            int otp = (int) (Math.random() * (9999 - 1000) + 1000);
            user.setOtp(otp);
            userdao.saveUser(user);
            javaMailSender.sendEmail(user.getEmail(), "This is Your Otp  " + otp, "Your Otp To Update Password");
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("OTP Sent To Email Id: " + email);
            responseStructure.setData("OTP Sent To The Email Of User");
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> validateOtp(int otp) throws Exception {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findByOtp(otp);
        if (user != null) {
            user.setPassword(user.getPassword());
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setData(user);
            responseStructure.setMessage("Success");
            return new ResponseEntity<ResponseStructure<User>>(responseStructure, HttpStatus.OK);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setData(null);
            responseStructure.setMessage("Otp Invalid");
            return new ResponseEntity<ResponseStructure<User>>(responseStructure, HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<ResponseStructure<User>> updateNewPassword(String password, int otp) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findByOtp(otp);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Password Not Found To Update ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Password Updated " + user);
            user.setPassword(password);
            int otp1 = (int) (Math.random() * (9999 - 1000) + 100000);
            user.setOtp(otp1);
            user = userdao.saveUser(user);
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> delete(long id) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userdao.findById(id);
        if (Objects.isNull(user)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User Doesn't Exists To Be Deleted");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            userdao.deletUser(user);
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User Deleted Successfully");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }
}

