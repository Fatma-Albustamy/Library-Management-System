package com.example.libraryManagement.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book")
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

public class Book extends CommonEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowingRecord> borrowingRecords;

    public Book(long id, String title, String author, String isbn, int publicationYear, int quantity, String description) {
        super();
        this.setId(id);
        this.title=title;
        this.author=author;
        this.isbn=isbn;
        this.publicationYear=publicationYear;
        this.quantity=quantity;
        this.description=description;
    }


    public  boolean canBorrow() {
        return quantity > 0;
    }

    public void borrow() {
        if (canBorrow()) {
            quantity--;
        }
    }

    public void returnBook() {
        quantity++;
    }
}
