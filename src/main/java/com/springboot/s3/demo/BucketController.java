package com.springboot.s3.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("bucket")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    AmazonS3ClientImp amazonS3ClientImp;
    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFileS3(file);
    }
/*
    @PostMapping("download")
    public ResponseEntity<byte[]> uploadFile(@RequestBody String file) {
        return  ResponseEntity.ok( this.amazonClient.download(file));
    }
*/
    @PostMapping("download-url")
    public String GeneratePresignedUrl(@RequestBody String file)
    {
        return amazonS3ClientImp.GeneratePresignedUrl(file);
    }

}
