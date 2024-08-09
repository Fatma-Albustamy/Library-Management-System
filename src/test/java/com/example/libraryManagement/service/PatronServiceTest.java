package com.example.libraryManagement.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.PatronDto;
import com.example.libraryManagement.model.PageableModel;
import com.example.libraryManagement.repository.PatronRepo;
import com.example.libraryManagement.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PatronServiceTest {

    @Mock
    private PatronRepo patronRepo;

    @Mock
    private CommonUtil commonUtil;

    @InjectMocks
    private PatronService patronService;

    @Test
    void testRetrieveAllPatrons() {
        List<Patron> patrons = Arrays.asList(
                new Patron(1L, "Fatma", "fatma@example.com", "1234567890"),
                new Patron(2L, "Albastamy", "Albastamy@example.com", "0987654321")
        );
        when(patronRepo.findAll()).thenReturn(patrons);
        List<PatronDto> result = patronService.retrieveAllPatrons();
        assertEquals(2, result.size());
        assertEquals("Fatma", result.get(0).getName());
        assertEquals("Albastamy", result.get(1).getName());
        verify(patronRepo, times(1)).findAll();
    }

    @Test
    void testRetrieveAllPatronsPageable() {
        List<Patron> patrons = Arrays.asList(
                new Patron(1L, "Fatma", "fatma@example.com", "1234567890")
        );
        Page<Patron> patronPage = new PageImpl<>(patrons);
        Pageable pageable = PageRequest.of(0, 10);
        when(patronRepo.findAll(pageable)).thenReturn(patronPage);
        PageableModel pageableModel = new PageableModel(0, 10);
        List<PatronDto> result = patronService.retrieveAllPatronsPageable(pageableModel);
        assertEquals(1, result.size());
        assertEquals("Fatma", result.get(0).getName());
        verify(patronRepo, times(1)).findAll(pageable);
    }

    @Test
    void testRetrievePatronById() {
        Patron patron = new Patron(1L, "Fatma", "Fatma@example.com", "1234567890");
        when(patronRepo.findById(1L)).thenReturn(Optional.of(patron));
        PatronDto result = patronService.retrievePatronById(1L);
        assertNotNull(result);
        assertEquals("Fatma", result.getName());
        verify(patronRepo, times(1)).findById(1L);
    }

    @Test
    void testAddPatron() {
        // Arrange
        PatronDto patronDto = new PatronDto(null, "Fatma", "Fatma@example.com", "1234567890");
        Patron patron = new Patron(1L, "Fatma", "Fatma@example.com", "1234567890");
        when(patronRepo.save(any(Patron.class))).thenReturn(patron);
        PatronDto result = patronService.addPatron(patronDto);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        verify(patronRepo, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron() throws BusinessException {
        PatronDto patronDto = new PatronDto(null, "Updated Fatma", "updated_fatma@example.com", "0987654321");
        Patron patron = new Patron(1L, "Fatma", "Fatma@example.com", "1234567890");
        when(commonUtil.checkPatronExistence(1L)).thenReturn(Optional.of(patron));
        PatronDto result = patronService.updatePatron(1L, patronDto);
        assertEquals("Updated Fatma", patron.getName());
        assertEquals("updated_fatma@example.com", patron.getEmail());
        verify(commonUtil, times(1)).checkPatronExistence(1L);
        verify(patronRepo, times(1)).save(patron);
    }

    @Test
    void testDeletePatron() throws BusinessException {
        Patron patron = new Patron(1L, "Fatma", "Fatma@example.com", "1234567890");
        when(commonUtil.checkPatronExistence(1L)).thenReturn(Optional.of(patron));
        patronService.deletePatron(1L);
        assertTrue(patron.getDeleted());
        verify(commonUtil, times(1)).checkPatronExistence(1L);
        verify(patronRepo, times(1)).save(patron);
    }
}
