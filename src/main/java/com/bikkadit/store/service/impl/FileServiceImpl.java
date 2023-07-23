package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.exception.BadApiRequest;
import com.bikkadit.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException
    {
        LOGGER.info("Fetching request to uploadFile() for file: {}",file);
        String originalFilename = file.getOriginalFilename();
        LOGGER.info("Filename: {}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path  + File.separator + fileNameWithExtension;
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg"))
        {
            // FIle save
            File folder = new File(path);
            if(!folder.exists()){

                // create the folder
                folder.mkdirs();
            }

            // upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            LOGGER.info("Completed Request For upload File: {}",file);
            return fileNameWithExtension;

        }else{
            throw new BadApiRequest(AppConstant.EXTENSION + extension);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException
    {
        LOGGER.info("Fetching request Inside getResource() for path: {}",path);

        String fullPath = path + File.separator+name;
        LOGGER.info("full path:{}",fullPath);

        InputStream inputStream = new FileInputStream(fullPath);

        LOGGER.info("Completed Request For getResource() ");

        return inputStream;
    }
}
