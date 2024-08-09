package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BusinessException;

import com.example.libraryManagement.repository.BorrowingRecordRepo;
import com.example.libraryManagement.utils.CommonUtil;
import com.example.libraryManagement.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class BorrowingService {

    static final Logger log = LoggerFactory.getLogger(BorrowingService.class);

    private final BorrowingRecordRepo borrowingRepo;
    private final CommonUtil commonUtil;

    public BorrowingService(BorrowingRecordRepo borrowingRepo, CommonUtil commonUtil) {
        this.borrowingRepo = borrowingRepo;
        this.commonUtil = commonUtil;
    }
    @Transactional
    public void borrowBook(Long bookId, Long patronId) throws BusinessException {
        Optional<Book> book = commonUtil.checkBookExistence(bookId);
        Optional<Patron> patron = commonUtil.checkPatronExistence(patronId);

        if (book.get().canBorrow() && !borrowingRepo.existsByBookIdAndPatronIdAndIsReturnedFalse(bookId,patronId)) {
            book.get().borrow();
            borrowingRepo.save(BorrowingRecord.builder().book(book.get())
                    .patron(patron.get())
                    .borrowingDate(new Date()).build());
        }else {
            throw new BusinessException(Constants.BOOK_NOT_BORROW);
        }
    }

    @Transactional
    public void returnBook(Long bookId, Long patronId) throws BusinessException {
        commonUtil.checkBookExistence(bookId);
        commonUtil.checkPatronExistence(patronId);
        BorrowingRecord borrowingRecord = borrowingRepo.findByBookIdAndPatronId(bookId,patronId);
        borrowingRecord.Return();
        borrowingRepo.save(borrowingRecord);
    }

}