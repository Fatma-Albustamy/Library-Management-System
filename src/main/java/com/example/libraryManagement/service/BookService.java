package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.*;

import com.example.libraryManagement.repository.BookRepo;
import com.example.libraryManagement.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Service
public class BookService {

    static final Logger log = LoggerFactory.getLogger(BookService.class);


    private final BookRepo bookRepo;

    private final CommonUtil commonUtil;

    public BookService(BookRepo bookRepo, CommonUtil commonUtil) {
        this.bookRepo = bookRepo;
        this.commonUtil = commonUtil;
    }


    public List<BookDto> retrieveAllBooks() {
        List<BookDto> bookDtos = new ArrayList<>();
        List<Book> books = bookRepo.findAll();
        books.stream().forEach(book -> {
            bookDtos.add(BookDto.builder().id(book.getId()).title(book.getTitle())
                    .author(book.getAuthor()).isbn(book.getIsbn()).publicationYear(book.getPublicationYear())
                    .quantity(book.getQuantity()).description(book.getDescription()).build());
        });
        return bookDtos;
    }

    public List<BookDto> retrieveAllBooksPageable(PageableModel pageableModel) {
        List<BookDto> bookDtos = new ArrayList<>();
        Pageable pageRequest = PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
        Page<Book> books = bookRepo.findAll(pageRequest);
        books.stream().forEach(book -> {
            bookDtos.add(BookDto.builder().id(book.getId()).title(book.getTitle())
                    .author(book.getAuthor()).isbn(book.getIsbn()).publicationYear(book.getPublicationYear())
                    .quantity(book.getQuantity()).description(book.getDescription()).build());
        });
        return bookDtos;
    }

    @Cacheable("books")
    public BookDto retrieveBookById(long id) {
        Optional<Book> book = bookRepo.findById(id);
        return book.map(value -> BookDto.builder().id(value.getId()).
                title(value.getTitle()).author(value.getAuthor()).isbn(value.getIsbn())
                .publicationYear(value.getPublicationYear())
                .quantity(value.getQuantity()).description(value.getDescription()).build()).orElse(new BookDto());
    }

    @CachePut(value = "books", key = "#bookDto.id")
    public BookDto addBook(BookDto bookDto) {
        Book book = bookRepo.save(Book.builder().title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .isbn(bookDto.getIsbn()).publicationYear(bookDto.getPublicationYear())
                .quantity(bookDto.getQuantity()).description(bookDto.getDescription()).build());
        bookDto.setId(book.getId());
        return bookDto;
    }


    public BookDto updateBook(Long id, BookDto bookDto) throws BusinessException {
        Optional<Book> book = commonUtil.checkBookExistence(id);
        book.get().setId(id);
        book.get().setTitle(bookDto.getTitle());
        book.get().setAuthor(bookDto.getAuthor());
        book.get().setPublicationYear(bookDto.getPublicationYear());
        book.get().setIsbn(bookDto.getIsbn());
        book.get().setQuantity(bookDto.getQuantity());
        book.get().setDescription(bookDto.getDescription());
        bookRepo.save(book.get());
        return bookDto;
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteBook(Long id) throws BusinessException {
        Optional<Book> book = commonUtil.checkBookExistence(id);
        book.get().setDeleted(true);
        bookRepo.save(book.get());
    }

}
