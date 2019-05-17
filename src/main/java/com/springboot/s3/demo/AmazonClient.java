package com.springboot.s3.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


import javax.annotation.PostConstruct;

import java.nio.ByteBuffer;
import java.util.Date;


@Service
public class AmazonClient {
    //this class use AWS SDK for Java Version 2.x
    private S3Client s3client;

    //@Value("${aws.s3.endpointurl}")
    //private String endpointUrl;
    //private String accessKey= "";
    //@Value("${aws.s3.secretkey}")
    //private String secretKey="";
    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @PostConstruct // run after the constractor because @Value bind after constructor
    private void initializeAmazon() {
        //this.s3client = S3Client.create(); // default region and default credential from application.property not secure
        // this.s3client=S3Client.builder().region(Region.US_EAST_2).build();


        //manuel credental not secure
        //AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        //this.s3client = S3Client.builder().credentialsProvider(StaticCredentialsProvider
        //        .create(awsCreds)).region(Region.US_EAST_1).build();

        //Using the Default Credential Provider Chain for accesskey and secretket on https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
        this.s3client = S3Client.builder().build();
    }

    public String uploadFileS3(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            String fileName = generateFileName(multipartFile);
            //fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;

            PutObjectResponse putObjectResponse = s3client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
                    RequestBody.fromByteBuffer(ByteBuffer.wrap(multipartFile.getBytes())));
            putObjectResponse = putObjectResponse;
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }


}
