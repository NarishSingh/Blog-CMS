package com.sg.blogcms.model;

import org.springframework.web.multipart.MultipartFile;

public interface ImageDao {

    /**
     * Upload and save an image file
     *
     * @param file      {MultipartFile} a valid image file
     * @param filename  {String} the original filename
     * @param directory {String} the directory name to store to
     * @return {String} the saved file's name
     */
    String saveImage(MultipartFile file, String filename, String directory);

    /**
     * Update and replace an image file
     *
     * @param file      {MultipartFile} a valid image file
     * @param filename  {String} the original filename of the update image
     * @param directory {String} the directory name to store to
     * @return {String} the updated file's name that was successfully updated
     */
    String updateImage(MultipartFile file, String filename, String directory);

    /**
     * Delete an image
     *
     * @param filename {String} the name of the file to be deleted
     * @return {boolean} true if successfully deleted
     */
    boolean deleteImage(String filename);
}
