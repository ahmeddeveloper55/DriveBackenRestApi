package com.example.DeiveC.repoitory;

import com.example.DeiveC.model.FileDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<FileDb,String> {
}
