package com.meloning.shop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    public static String uploadFile(String uploadRoot, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid + extension;
        String fileUploadFullUrl = uploadRoot + File.separator + savedFileName;

        FileOutputStream fileOutputStream = new FileOutputStream(fileUploadFullUrl);
        fileOutputStream.write(fileData);
        fileOutputStream.close();

        return savedFileName;
    }

    public static void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
