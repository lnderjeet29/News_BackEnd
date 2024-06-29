package com.codefylabs.Maple.Leaf.persistence.services

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.codefylabs.Maple.Leaf.business.gateway.ImageUploadService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Service
class ImageUploadServiceImpl(var s3Client: AmazonS3) :ImageUploadService{


    @Value("\${aws.s3.bucket}")
    private val bucketName = "maple-leaf-images"

    @Value("\${aws.s3.region}")
    private val bucketRegion = "ap-southeast-2"

    // for returning the URL
    @Transactional
    override fun uploadFile(multipartFile: MultipartFile): String? {
        return try {
            val file = convertMultiPartFileToFile(multipartFile)
            val fileUrl = uploadFileToS3Bucket(file)
            file.deleteOnExit()  // To remove the file locally created in the project folder.
            fileUrl
        } catch (ex: AmazonServiceException) {
            println("Error while uploading file = ${ex.message}")
            null
        }

    }

    private fun convertMultiPartFileToFile(multipartFile: MultipartFile): File {
        val file = File(multipartFile.originalFilename!!)
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(multipartFile.bytes)
            }
        } catch (ex: IOException) {
            println("Error converting the multi-part file to file = ${ex.message}")
        }
        return file
    }

    @Transactional
    private fun uploadFileToS3Bucket(file: File): String {
        val putObjectRequest = PutObjectRequest(bucketName, file.name, file)
        s3Client.putObject(putObjectRequest)
        return s3Client.getUrl(bucketName, file.name).toString()

    }


}

