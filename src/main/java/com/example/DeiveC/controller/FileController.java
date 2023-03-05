package com.example.DeiveC.controller;

import com.example.DeiveC.model.FileDb;
import com.example.DeiveC.response.FileResponse;
import com.example.DeiveC.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.IDN;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/files")
public class FileController {
    @Autowired
    FileService service;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        String msg = "";
        try {
            FileDb fileDb = service.saveFile(file);
            String fileUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/files/downloadFile/")
                    .path(fileDb.getId())
                    .toUriString();
            FileResponse response = new FileResponse(fileDb.getId(), fileDb.getName(), fileUrl, file.getContentType(), file.getSize());
            msg = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            System.out.print(e);
        }
        return null;
        }

//
//    @PostMapping("/UploadMultipleFiles")
//    public List<ResponseEntity<String>> uploadMutipleFiles(@RequestParam("files") MultipartFile[] files){
//        return Arrays.stream(files).map(this::uploadFile).collect(Collectors.toList());
//    }
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable String fileName) throws Exception {
      FileDb model = service.getFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(model.getType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + model.getName() + "\"")
                .body(new ByteArrayResource(model.getData()));
    }
    @GetMapping("/files")
    public ResponseEntity<List<FileResponse>> getListFiles() {
        List<FileResponse> files = service.getListOfFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/files/downloadFile/")
                    .path(dbFile.getId())
                    .toUriString();

            return new FileResponse(
                    dbFile.getId(),
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) throws Exception {
        FileDb fileDB = service.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
    @PutMapping("/rename/{id}")
    public ResponseEntity<FileResponse> update(@RequestBody FileDb db , @PathVariable String id){
        FileDb fileDb =  service.upldate(db,id);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/files/downloadFile/")
                .path(fileDb.getId())
                .toUriString();
        FileResponse response =  new FileResponse(fileDb.getId(),fileDb.getName(),fileDownloadUri,fileDb.getType(),fileDb.getData().length);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/DeleteOneFile/{ID}")
    public String DeleteOne(@PathVariable String ID){
        service.deleteOne(ID);
            return "file: " + ID + " Deleted";

    }
    @DeleteMapping("/deleteAll")
    public String DeleteAll(){
        boolean ifSuccessful  = service.deleteAll();
        if (ifSuccessful){
            return  "All Files Deleted ";
        }else{
            return "The Deletion Failed";
        }
    }


}
