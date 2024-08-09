package com.example.libraryManagement.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "patron")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patron extends CommonEntity {


    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_no")
    private String mobileNo;

    @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowingRecord> borrowingRecords;

    public Patron(long id, String name, String mail, String mobileNo) {
        super();
        this.setId(id);
        this.name=name;
        this.email=email;
        this.mobileNo=mobileNo;
    }
}


