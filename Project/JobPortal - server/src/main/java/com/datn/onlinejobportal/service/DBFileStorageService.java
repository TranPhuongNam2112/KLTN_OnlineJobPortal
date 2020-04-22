package com.datn.onlinejobportal.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.datn.onlinejobportal.exception.FileStorageException;
import com.datn.onlinejobportal.exception.MyFileNotFoundException;
import com.datn.onlinejobportal.model.DBFile;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.model.UserFile;
import com.datn.onlinejobportal.repository.DBFileRepository;
import com.datn.onlinejobportal.repository.UserFileRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.UserPrincipal;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserFileRepository userFileRepository;

    public DBFile storeFile(MultipartFile file, UserPrincipal currentUser) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            User user = userRepository.getOne(currentUser.getId());
            UserFile userFile = new UserFile(user, dbFile);
            dbFileRepository.save(dbFile);
            userFileRepository.save(userFile);

            return dbFile;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}