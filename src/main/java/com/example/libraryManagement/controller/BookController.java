package com.example.libraryManagement.controller;

import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.BookDto;
import com.example.libraryManagement.model.PageableModel;
import com.example.libraryManagement.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<List<BookDto>> retrieveAllBooks() {
        return new ResponseEntity<>(bookService.retrieveAllBooks(), HttpStatus.OK);
    }

    @PostMapping("retrieve/pageable")
    public ResponseEntity<List<BookDto>> retrieveAllBooksPageable(@RequestBody PageableModel pageableModel) {
        return new ResponseEntity<>(bookService.retrieveAllBooksPageable(pageableModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> retrieveBookById(@PathVariable("id") long id) {
        return new ResponseEntity<>(bookService.retrieveBookById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BookDto> AddBook( @RequestBody @Valid BookDto bookDto) {
        return new ResponseEntity<>(bookService.addBook(bookDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook( @PathVariable("id") Long id,@RequestBody @Valid BookDto bookDto) throws BusinessException {
        return new ResponseEntity<>(bookService.updateBook(id,bookDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook( @PathVariable("id") Long id) throws BusinessException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
