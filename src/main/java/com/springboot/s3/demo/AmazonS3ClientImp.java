package com.springboot.s3.demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;



import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.Date;

@Service
public class AmazonS3ClientImp {

    //this class use AWS SDK for Java Version 1.11
    private AmazonS3 s3client;

    //@Value("${aws.s3.endpointurl}")
    //private String endpointUrl;
    @Value("${aws.s3.bucketname}")
    private String bucketName;
    //@Value("${aws.s3.accesskey}")
    //private String accessKey= "";
    //@Value("${aws.s3.secretkey}")
    //private String secretKey="";

    //@Value("${aws.s3.clientRegion}")
    //String clientRegion;

    @PostConstruct
    private void initializeAmazon() {
        //AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        //this.s3client = new AmazonS3Client(credentials);
        /*
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {

                return credentials;
            }

            @Override
            public void refresh() {

            }
        }).build();

         */
        this.s3client = AmazonS3ClientBuilder.standard()
                //.withRegion(Regions.US_EAST_1)
                .build();
    }
/*
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;

            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }
*/

    public String GeneratePresignedUrl(String filename) {
        try {

            // Set the presigned URL to expire after one hour.
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 ;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, filename)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

            System.out.println("Pre-Signed URL: " + url.toString());
            return url.toString();
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return " excepton";
    }
}
