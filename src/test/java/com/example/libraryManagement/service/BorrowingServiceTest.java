package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.repository.BorrowingRecordRepo;
import com.example.libraryManagement.utils.CommonUtil;
import com.example.libraryManagement.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {
    @Mock
    private BorrowingRecordRepo borrowingRepo;

    @Mock
    private CommonUtil commonUtil;

    @InjectMocks
    private BorrowingService borrowingService;


    @Test
    void testBorrowBookSuccess() throws BusinessException {
        Book book = new Book(1L, "Title", "Author", "ISBN", 2021, 10, "Description");
        Patron patron = new Patron(1L, "Patron Name", "patron@example.com", "1234567890");
        when(commonUtil.checkBookExistence(1L)).thenReturn(Optional.of(book));
        when(commonUtil.checkPatronExistence(1L)).thenReturn(Optional.of(patron));
        when(borrowingRepo.existsByBookIdAndPatronIdAndIsReturnedFalse(1L, 1L)).thenReturn(false);

        borrowingService.borrowBook(1L, 1L);

        verify(borrowingRepo, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBookAlreadyBorrowed() throws BusinessException {
        Book book = new Book(1L, "Title", "Author", "ISBN", 2021, 10, "Description");
        Patron patron = new Patron(1L, "Patron Name", "patron@example.com", "1234567890");
        when(commonUtil.checkBookExistence(1L)).thenReturn(Optional.of(book));
        when(commonUtil.checkPatronExistence(1L)).thenReturn(Optional.of(patron));
        when(borrowingRepo.existsByBookIdAndPatronIdAndIsReturnedFalse(1L, 1L)).thenReturn(true);
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });
        assertEquals(Constants.BOOK_NOT_BORROW, thrown.getMessage());
    }

    @Test
    void testReturnBookSuccess() throws BusinessException {
        Book book = new Book(1L, "Title", "Author", "ISBN", 2021, 10, "Description");
        Patron patron = new Patron(1L, "Patron Name", "patron@example.com", "1234567890");
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .borrowingDate(new Date())
                .build();

        when(commonUtil.checkBookExistence(1L)).thenReturn(Optional.of(book));
        when(commonUtil.checkPatronExistence(1L)).thenReturn(Optional.of(patron));
        when(borrowingRepo.findByBookIdAndPatronId(1L, 1L)).thenReturn(borrowingRecord);
        borrowingService.returnBook(1L, 1L);
        verify(borrowingRepo, times(1)).save(borrowingRecord);
        assertTrue(borrowingRecord.isReturned());  // Adjust this based on your actual implementation
    }
}