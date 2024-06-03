package com.project.billing.Dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true,nullable = false)
    private  String email;
    private  String password;
    @Column(unique = true,nullable = false)
    private String  phone;
    private  String role;
    private int otp;
    @JsonGetter
    public String getRole() {
        return role;
    }
    @JsonIgnore
    public void setRole(String role) {
        this.role = role;
    }


}
