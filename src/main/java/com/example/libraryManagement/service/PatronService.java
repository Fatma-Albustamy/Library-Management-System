package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BusinessException;
import com.example.libraryManagement.model.PatronDto;

import com.example.libraryManagement.model.PageableModel;

import com.example.libraryManagement.repository.PatronRepo;
import com.example.libraryManagement.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    static final Logger log = LoggerFactory.getLogger(PatronService.class);


    private final PatronRepo patronRepo;
    private final CommonUtil commonUtil;

    public PatronService(PatronRepo patronRepo, CommonUtil commonUtil) {
        this.patronRepo = patronRepo;
        this.commonUtil = commonUtil;
    }


    public List<PatronDto> retrieveAllPatrons() {
        List<PatronDto> patronDtos = new ArrayList<>();
        List<Patron> patrons = patronRepo.findAll();
        patrons.stream().forEach(patron -> {
            patronDtos.add(PatronDto.builder().id(patron.getId()).name(patron.getName())
                    .email(patron.getEmail()).mobileNo(patron.getMobileNo()).build());
        });
        return patronDtos;
    }

    public List<PatronDto> retrieveAllPatronsPageable(PageableModel pageableModel) {
        List<PatronDto> patronDtos = new ArrayList<>();
        Pageable pageRequest = PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
        Page<Patron> patrons = patronRepo.findAll(pageRequest);
        patrons.stream().forEach(patron -> {
            patronDtos.add(PatronDto.builder().id(patron.getId()).name(patron.getName())
                    .email(patron.getEmail()).mobileNo(patron.getMobileNo()).build());
        });
        return patronDtos;
    }

    @Cacheable("books")
    public PatronDto retrievePatronById(long id) {
        Optional<Patron> patron = patronRepo.findById(id);
        return patron.map(value -> PatronDto.builder().id(value.getId()).
                name(value.getName()).email(value.getEmail()).mobileNo(value.getMobileNo()).build()).orElse(new PatronDto());
    }

    @CachePut(value = "patrons", key = "#patronDto.id")
    public PatronDto addPatron(PatronDto patronDto) {
        Patron patron = patronRepo.save(Patron.builder().name(patronDto.getName())
                .email(patronDto.getEmail()).mobileNo(patronDto.getMobileNo()).build());
        patronDto.setId(patron.getId());
        return patronDto;
    }


    public PatronDto updatePatron(Long id, PatronDto patronDto) throws BusinessException {
        Optional<Patron> patron = commonUtil.checkPatronExistence(id);
        patron.get().setId(id);
        patron.get().setEmail(patronDto.getEmail());
        patron.get().setMobileNo(patronDto.getMobileNo());
        patron.get().setName(patronDto.getName());
        patronRepo.save(patron.get());
        return patronDto;
    }


    @CacheEvict(value = "patrons", allEntries = true)
    public void deletePatron(Long id) throws BusinessException {
        Optional<Patron> patron = commonUtil.checkPatronExistence(id);
        patron.get().setDeleted(true);
        patronRepo.save(patron.get());
    }


}