package com.example.libraryManagement.controller;

import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.PageableModel;
import com.example.libraryManagement.model.PatronDto;
import com.example.libraryManagement.service.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping()
    public ResponseEntity<List<PatronDto>> retrieveAllPatrons() {
        return new ResponseEntity<>(patronService.retrieveAllPatrons(), HttpStatus.OK);
    }

    @PostMapping("retrieve/pageable")
    public ResponseEntity<List<PatronDto>> retrieveAllPatronsPageable(@RequestBody PageableModel pageableModel) {
        return new ResponseEntity<>(patronService.retrieveAllPatronsPageable(pageableModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDto> retrievePatronById(@PathVariable("id") long id) {
        return new ResponseEntity<>(patronService.retrievePatronById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PatronDto> AddPatron( @RequestBody @Valid PatronDto patronDto) {
        return new ResponseEntity<>(patronService.addPatron(patronDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDto> updatePatron( @PathVariable("id") Long id,@RequestBody @Valid PatronDto patronDto) throws BusinessException {
        return new ResponseEntity<>(patronService.updatePatron(id,patronDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePatron( @PathVariable("id") Long id) throws BusinessException {
        patronService.deletePatron(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
