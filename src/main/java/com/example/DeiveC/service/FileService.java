package com.example.DeiveC.service;

import com.example.DeiveC.Exceptions.FileNotFoundException;
import com.example.DeiveC.Exceptions.FileSavedException;
import com.example.DeiveC.constant.FileErrors;
import com.example.DeiveC.model.FileDb;
import com.example.DeiveC.repoitory.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    FileRepo fileRepo;
    public FileDb saveFile(MultipartFile file) throws IOException {
        String filename =  StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDb fileDb = new FileDb(filename,file.getContentType(),file.getBytes());
        return fileRepo.save(fileDb);

    }

   public FileDb getFile(String fileId) throws Exception {
       return fileRepo.findById(fileId).orElseThrow(()-> new FileNotFoundException(FileErrors.FILE_NOT_FOUND+fileId));
   }
    public Stream<FileDb> getListOfFiles(){
        return fileRepo.findAll().stream();
    }
    public void deleteOne(String fileName){
            fileRepo.deleteById(fileName);
    }
    public FileDb upldate(FileDb db , String id){
        FileDb fileDb = fileRepo.findById(id).get();
        fileDb.setName(db.getName());
        fileRepo.save(fileDb);
        return fileDb;
    }
    public boolean deleteAll(){
        try {
            fileRepo.deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
