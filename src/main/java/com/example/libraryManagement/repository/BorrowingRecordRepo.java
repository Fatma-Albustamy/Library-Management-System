package com.example.libraryManagement.repository;

import com.example.libraryManagement.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BorrowingRecordRepo extends JpaRepository<BorrowingRecord,Long> {

    BorrowingRecord findByBookIdAndPatronId(Long bookId,Long patronId);
    boolean existsByBookIdAndPatronIdAndIsReturnedFalse(Long bookId,Long patronId);
}
