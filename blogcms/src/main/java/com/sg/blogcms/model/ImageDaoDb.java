package com.sg.blogcms.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class ImageDaoDb implements ImageDao {

    private final String RESOURCE_ROOT = "C:/Users/naris/Documents/Work/TECHHIRE/REPOSITORY/SG-DDWA-Mastery-Blog-CMS/blogcms/src/main/resources/static/";
    private final String UPLOAD_DIR = "images/uploads/";

    @Override
    public String saveImage(MultipartFile file, String filename, String directory) {
        String savedAs = "";

        String mimetype = file.getContentType();

        if (mimetype != null && mimetype.split("/")[0].equals("image")) {
            String original = file.getOriginalFilename();
            String[] parts = original.split("[.]");
            filename = filename + "." + parts[parts.length - 1];

            try {
                String fullPath = RESOURCE_ROOT + UPLOAD_DIR + directory + "/";
                File dir = new File(fullPath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Path path = Paths.get(fullPath + filename);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                savedAs = UPLOAD_DIR + directory + "/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return savedAs;
    }

    @Override
    public String updateImage(MultipartFile file, String filename, String directory) {
        String savedAs = "";

        if (filename != null && !filename.isEmpty()) {
            File oldFile = new File(RESOURCE_ROOT + filename);
            oldFile.delete();

            String[] parts = filename.split("/");
            filename = parts[parts.length - 1].split("[.]")[0];
        } else {
            filename = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        }

        String mimetype = file.getContentType();
        if (mimetype != null && mimetype.split("/")[0].equals("image")) {
            String original = file.getOriginalFilename();
            String[] parts = original.split("[.]");
            filename = filename + "." + parts[parts.length - 1];

            try {
                String fullPath = RESOURCE_ROOT + UPLOAD_DIR + directory + "/";
                File dir = new File(fullPath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Path path = Paths.get(fullPath + filename);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                savedAs = UPLOAD_DIR + directory + "/" + filename;
            } catch (IOException e) {
                return null;
            }
        }

        return savedAs;
    }

    @Override
    public boolean deleteImage(String filename) {
        File oldFile = new File(RESOURCE_ROOT + filename);

        return oldFile.delete();
    }

}
