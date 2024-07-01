package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.news.PictureType
import org.springframework.web.multipart.MultipartFile


interface ImageUploadService {

    fun uploadImage(multipartFile: MultipartFile, pictureType: PictureType, id:String): String?
    fun uploadFile(multipartFile: MultipartFile,fileName:String): String?

}







