package com.userdb.mobileapp.service.impl;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class GoogleCloudStorageServiceImpl {

    @Value("${google.cloud.bucket.name}")
    private String bucketName;

    @Value("${google.cloud.credentials.location}")
    private String credentialsFilePath;

    // Phương thức upload ảnh từ MultipartFile lên Google Cloud Storage
    public String uploadImageToGoogleCloudStorage(String imageName, MultipartFile file) throws IOException {
        // Đọc tệp JSON từ resources
        InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("appmobile-account-service.json");

        if (credentialsStream == null) {
            throw new IOException("Service account credentials file not found.");
        }

        // Xác thực sử dụng service account credentials JSON
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(credentialsStream))
                .build().getService();

        // Tạo Blob (đối tượng) trên Cloud Storage
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, "images/" + imageName).build();

        // Tải ảnh lên Google Cloud Storage
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Trả về đường dẫn URL của ảnh đã được tải lên
        return "https://storage.googleapis.com/" + bucketName + "/images/" + imageName;
    }

}
