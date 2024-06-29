package com.codefylabs.Maple.Leaf.rest.config
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider
//import com.amazonaws.auth.BasicAWSCredentials
//import com.amazonaws.client.builder.AwsClientBuilder
//import com.amazonaws.services.s3.AmazonS3
//import com.amazonaws.services.s3.AmazonS3ClientBuilder
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//@Configuration
//class AwsS3Configuration {
//    @Value("\${aws.accessKey}")
//    private lateinit var accessKey: String
//    @Value("\${aws.secretKey}")
//    private lateinit var secretKey: String
//    @Value("\${aws.s3.region}")
//    private lateinit var region: String
//    @Value("\${aws.s3.endpoint}")
//    private lateinit var endpoint: String
//    @Bean
//    fun amazonS3Client(): AmazonS3 {
//        val credentials = BasicAWSCredentials(accessKey, secretKey)
//        return AmazonS3ClientBuilder.standard()
//            .withCredentials(AWSStaticCredentialsProvider(credentials))
//            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
//            .build()
//    }
//}
//
//
//
//
//
//
//
