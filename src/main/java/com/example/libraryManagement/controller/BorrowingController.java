package com.example.libraryManagement.controller;

import com.example.libraryManagement.exception.BusinessException;

import com.example.libraryManagement.service.BorrowingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/borrowing")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) throws BusinessException {
        borrowingService.borrowBook(bookId,patronId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) throws BusinessException {
        borrowingService.returnBook(bookId,patronId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
