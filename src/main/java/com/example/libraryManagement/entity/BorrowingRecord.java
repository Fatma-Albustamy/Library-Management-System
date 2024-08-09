package com.example.libraryManagement.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "borrowing_record")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecord extends CommonEntity {

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @Column(name = "borrowing_date")
    private Date borrowingDate;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "is_returned")
    private boolean isReturned;

    public BorrowingRecord(Book book, Patron patron) {
        this.book = book;
        this.patron = patron;
        this.borrowingDate = new Date();
    }

    public void Return() {
        setReturned(true);
        setReturnDate(new Date());
        book.returnBook();
    }
}

