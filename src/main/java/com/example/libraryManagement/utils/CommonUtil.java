package com.example.libraryManagement.utils;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.repository.BookRepo;
import com.example.libraryManagement.repository.PatronRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public  class CommonUtil {
    private final BookRepo bookRepo;
    private final PatronRepo patronRepo;

    public CommonUtil(BookRepo bookRepo, PatronRepo patronRepo) {
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }
    public Optional<Book> checkBookExistence(Long id) throws BusinessException {
        Optional<Book> book = bookRepo.findById(id);
        if (!book.isPresent())
            throw new BusinessException(Constants.BOOK_NOT_FOUND);
        return book;
    }


    public Optional<Patron> checkPatronExistence(Long id) throws BusinessException {
        Optional<Patron> patron = patronRepo.findById(id);
        if (!patron.isPresent())
            throw new BusinessException(Constants.PATRON_NOT_FOUND);
        return patron;
    }
}
