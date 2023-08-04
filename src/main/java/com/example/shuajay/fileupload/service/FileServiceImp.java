package com.example.shuajay.fileupload.service;

import com.example.shuajay.fileupload.model.FileEntity;
import com.example.shuajay.fileupload.repo.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImp implements FileService {
    private final FileRepository fileRepository;

    @Override
    @Transactional
    public void saveFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setData(file.getContentType());

        fileRepository.save(fileEntity);
    }

    @Override
    public Optional<FileEntity> getFileById(Long id) {
        fileRepository.findById(id);
        return null;
    }

    @Override
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

}
