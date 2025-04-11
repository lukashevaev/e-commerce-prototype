package com.bubusyaka.demo.model.dto;

import com.bubusyaka.demo.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private Long age;
    private String city;
    private Role role;
    private String userPassword;

}
