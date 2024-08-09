package com.example.libraryManagement.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Author cannot be null")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    private Integer publicationYear;

    @NotNull(message = "ISBN cannot be null")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be a 13-digit number")
    private String isbn;

    private String description;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}

