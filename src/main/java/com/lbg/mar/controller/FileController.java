package com.lbg.mar.controller;

import com.lbg.mar.dto.FileDto;
import com.lbg.mar.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class FileController {

    private final FileService svc;

    public FileController(FileService svc) {
        this.svc = svc;
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postFile(@RequestBody FileDto dto) {
        svc.save(dto);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<FileDto> getById(@PathVariable String fileName) throws Exception {
        Optional<FileDto> file = svc.getFile(fileName);
        if (file.isPresent()) {
            return new ResponseEntity<>(file.get(), HttpStatus.OK);
        } else {
            throw new Exception();
        }
    }

}
