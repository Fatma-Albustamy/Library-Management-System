package com.example.libraryManagement.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.BookDto;
import com.example.libraryManagement.model.PageableModel;
import com.example.libraryManagement.repository.BookRepo;
import com.example.libraryManagement.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepo bookRepo;

    @Mock
    private CommonUtil commonUtil;

    @InjectMocks
    private BookService bookService;


    @Test
    void testRetrieveAllBooks() {
        // Arrange
        List<Book> books = Arrays.asList(
                new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1"),
                new Book(2L, "Title2", "Author2", "ISBN2", 2022, 5, "Description2")
        );
        when(bookRepo.findAll()).thenReturn(books);
        List<BookDto> result = bookService.retrieveAllBooks();
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
        verify(bookRepo, times(1)).findAll();
    }

    @Test
    void testRetrieveAllBooksPageable() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1")
        );
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepo.findAll(pageable)).thenReturn(bookPage);
        PageableModel pageableModel = new PageableModel(0, 10);
        List<BookDto> result = bookService.retrieveAllBooksPageable(pageableModel);
        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        verify(bookRepo, times(1)).findAll(pageable);
    }

    @Test
    void testRetrieveBookById() {

        Book book = new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1");
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        BookDto result = bookService.retrieveBookById(1L);
        assertNotNull(result);
        assertEquals("Title1", result.getTitle());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testAddBook() {
        BookDto bookDto = new BookDto(null, "Title1", "Author1", 2021, "22222222222", "Description1", 1);
        Book book = new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1");
        when(bookRepo.save(any(Book.class))).thenReturn(book);
        BookDto result = bookService.addBook(bookDto);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() throws BusinessException {
        BookDto bookDto = new BookDto(null, "UpdatedTitle", "UpdatedAuthor", 2022, "UpdatedISBN", "UpdatedDescription", 5);
        Book book = new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1");
        when(commonUtil.checkBookExistence(1L)).thenReturn(Optional.of(book));
        BookDto result = bookService.updateBook(1L, bookDto);
        assertEquals("UpdatedTitle", book.getTitle());
        assertEquals("UpdatedAuthor", book.getAuthor());
        verify(commonUtil, times(1)).checkBookExistence(1L);
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    void testDeleteBook() throws BusinessException {
        Book book = new Book(1L, "Title1", "Author1", "ISBN1", 2021, 10, "Description1");
        when(commonUtil.checkBookExistence(1L)).thenReturn(Optional.of(book));
        bookService.deleteBook(1L);
        assertTrue(book.getDeleted());

        verify(commonUtil, times(1)).checkBookExistence(1L);
        verify(bookRepo, times(1)).save(book);
    }
}