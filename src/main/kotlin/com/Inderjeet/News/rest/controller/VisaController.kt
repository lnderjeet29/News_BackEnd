package com.Inderjeet.News.rest.controller

import com.Inderjeet.News.persistence.entities.SubVisa
import com.Inderjeet.News.persistence.entities.VisaData
import com.Inderjeet.News.rest.dto.CommonResponse
import com.Inderjeet.News.rest.dto.others.VisaDataDto
import com.Inderjeet.News.rest.dto.others.toDto
import lombok.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class VisaController(
    val visaDataService: com.Inderjeet.News.business.gateway.VisaDataService
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