package ru.pugart.ext.ui.dto;

import lombok.Data;

@Data
public class UserDto {
    private String phone;
    private String email;
    private String password;
    public String firstname;
    public String lastname;
}
