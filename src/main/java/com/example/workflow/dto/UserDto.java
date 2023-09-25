package com.example.workflow.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private String lastName;
    private String firstName;
    private String birthDay;
}
