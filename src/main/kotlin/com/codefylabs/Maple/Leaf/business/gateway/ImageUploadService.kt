package com.codefylabs.Maple.Leaf.business.gateway

import org.springframework.web.multipart.MultipartFile


interface ImageUploadService {


    fun uploadFile(multipartFile: MultipartFile): String?

}







