package com.lbg.mar.service;

import com.hazelcast.core.IMap;
import com.lbg.mar.dto.FileDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    private final IMap<String, String> cache;


    public FileService(IMap<String, String> cache) {
        this.cache = cache;
    }

    public String save(FileDto dto) {
        return cache.put(dto.getName(), dto.getContents());
    }


    public Optional<FileDto> getFile(String fileName) {
        String value = cache.get(fileName);
        FileDto file = new FileDto();
        file.setName(fileName);
        file.setContents(value);
        return Optional.of(file);
    }
}
