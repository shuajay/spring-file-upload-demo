package com.example.shuajay.fileupload.service;

import com.example.shuajay.fileupload.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {
    void saveFile(MultipartFile file) throws IOException;
    Optional<FileEntity> getFileById(Long id);
    List<FileEntity> getAllFiles();
    void deleteFile(Long id);
}
