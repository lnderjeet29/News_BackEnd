package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.news.PictureType
import org.springframework.web.multipart.MultipartFile


interface ImageUploadService {

    fun uploadImage(multipartFile: MultipartFile, pictureType: PictureType, id:String): String?
    fun uploadFile(multipartFile: MultipartFile,fileName:String): String?

}







