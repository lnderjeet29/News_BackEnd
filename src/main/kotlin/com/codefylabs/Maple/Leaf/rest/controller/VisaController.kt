package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.VisaDataService
import com.codefylabs.Maple.Leaf.persistence.entities.SubVisa
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import com.codefylabs.Maple.Leaf.rest.dto.FAQDto
import com.codefylabs.Maple.Leaf.rest.dto.others.VisaDataDto
import com.codefylabs.Maple.Leaf.rest.dto.others.toDto
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class VisaController(
    val visaDataService: VisaDataService
) {

    @GetMapping("/visa")
    fun getVisaDataByCategory(
        @RequestParam(name = "category") category: String
    ): ResponseEntity<CommonResponse<List<VisaDataDto>>> {
        return try {
            val faqs = visaDataService.getVisaDataByCategory(category.trim().lowercase()).map {it.toDto()}
            ResponseEntity.ok().body(CommonResponse(message = "FAQs retrieved successfully", status = true, data = faqs))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/admin/visa/create")
    fun storeVisaData(
        @RequestBody visaDataList: VisaDataDto,
        @RequestParam(name = "category") category: String
    ): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            val visaEntities = VisaData(
                    title = visaDataList.title,
                    content = visaDataList.content,
                    key = category.trim().lowercase(),
                    subVisa = visaDataList.subVisaData.map { subDto ->
                        SubVisa(
                            question = subDto.question,
                            answer = subDto.answer
                        )
                    }
                )
            visaDataService.saveVisaData(visaEntities)
            ResponseEntity.ok().body(CommonResponse(message = "Visa Data Uploaded Successfully.", status = true, data = true))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

}