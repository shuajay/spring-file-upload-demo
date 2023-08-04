package com.example.shuajay.fileupload.controller;

import com.example.shuajay.fileupload.model.FileEntity;
import com.example.shuajay.fileupload.service.FileServiceImp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//@RestController
@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
public class FileController {
    private final FileServiceImp fileServiceImp;


    @GetMapping("/file")
    public String showUploadForm(Model model){
        model.addAttribute("message", null);
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model){
        if (file.isEmpty()){
            model.addAttribute("message", "Please select a file to upload");
            return "upload";
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert extension != null;
        if (!extension.equals("pdf")){
            model.addAttribute("message", "Only PDF files are allowed");
            return "upload";
        }
        try{
            fileServiceImp.saveFile(file);
            model.addAttribute("message", "File uploaded successfully");
            model.addAttribute("file", file);
            return "upload";
        }catch (IOException e){
            model.addAttribute("message", "Failed to upload file");
            return "upload";
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> downloadFile(@PathVariable("id") Long id){
        Optional<FileEntity> fileEntityOptional = fileServiceImp.getFileById(id);
        if (fileEntityOptional.isPresent()){
            FileEntity fileEntity = fileEntityOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileEntity.getData().length()));
            return new ResponseEntity<>(fileEntity.getData(), headers, HttpStatus.OK);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view")
    public String getAllFiles(Model model){
        List<FileEntity> files = fileServiceImp.getAllFiles();
        model.addAttribute("files", files);
        return "view";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Long id){
        fileServiceImp.deleteFile(id);
        return "redirect:/";
    }
//    @PostMapping("/upload")
//    public ResponseEntity<String>handleFileUpload(@RequestParam("file")MultipartFile file){
//        if (file.isEmpty()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload");
//        }
//        try{
//            fileServiceImp.saveFile(file);
//            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
//        }catch (IOException e){
//           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
//        }
//    }
}
