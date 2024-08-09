package com.example.libraryManagement.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronDto {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Email(message = "Please provide a valid email address")
    private String email;

    @Pattern(regexp = "^\\d{12}$", message = "Mobile number must be 12 digits")
    private String mobileNo;


}
