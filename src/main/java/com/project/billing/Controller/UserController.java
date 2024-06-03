package com.project.billing.Controller;

import com.project.billing.Dto.ResponseStructure;
import com.project.billing.Dto.User;
import com.project.billing.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<ResponseStructure<User>> save(@RequestBody User user) {
        return userService.save(user);
    }
    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<ResponseStructure<User>> findById(@PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping(value = "/findByPhone/{phone}")
    public ResponseEntity<ResponseStructure<User>> findByPhone(@PathVariable String phone) {
        return userService.findByPhone(phone);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<ResponseStructure<Page<User>>> getAll(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "5") int pageSize, String field) {

        return userService.findAll(offset, pageSize, field);
    }
    @PostMapping(value = "/updateUser/{id}")
    public ResponseEntity<ResponseStructure<User>> updateUser(@RequestBody User user,@PathVariable long id){
        return userService.updateUser(user);
    }
    @PatchMapping(value = "/updateNewPassword/{otp}/{password}")
    public ResponseEntity<ResponseStructure<User>> updateNewPassword(@PathVariable String password, @PathVariable int otp) {
        return userService.updateNewPassword(password,otp);
    }

    @PostMapping(value = "/login/{email}/{password}")
    public ResponseEntity<ResponseStructure<User>> login(@PathVariable String email, @PathVariable String password) {
        return userService.login(email, password);
    }

    @PostMapping(value = "/forgotpassword")
    public ResponseEntity<ResponseStructure<String>> forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

    @GetMapping(value = "/validateOTP/{otp}")
    public ResponseEntity<ResponseStructure<User>> validateOTP(@PathVariable int otp) throws Exception {
        return userService.validateOtp(otp);
    }
    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseStructure<User>> delete(@RequestParam long id) {
        return userService.delete(id);
    }

    @GetMapping(value = "/findByEmail")
    public  ResponseEntity<ResponseStructure<User>> findByEmail(@RequestParam String email){
        return userService.findByEmail(email);
    }
}
