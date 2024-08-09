package com.example.libraryManagement.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Setter
@Getter
@Builder
public class BorrowingRecordDto {

    private BookDto bookDto;

    private PatronDto patronDto;

    private Date borrowingDate;

    private Date returnDate;

}

