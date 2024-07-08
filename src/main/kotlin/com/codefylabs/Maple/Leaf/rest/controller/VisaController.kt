package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.VisaDataService
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import com.codefylabs.Maple.Leaf.rest.dto.others.VisaDataDto
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
            val visaDataList = visaDataService.getVisaDataByCategory(category.lowercase())
            ResponseEntity.ok().body(CommonResponse(message = "Fetched successfully", status = true, data = visaDataList))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(CommonResponse(message = e.message ?: "Something went wrong!", status = false))
        }
    }

    @PostMapping("/admin/visa/create")
    fun storeVisaData(@RequestBody visaDataList: List<VisaDataDto>, @RequestParam(name = "category") category: String): ResponseEntity<CommonResponse<Nothing>> {
        return try {
            val visaEntities = visaDataList.map { dto ->
                VisaData(
                    title = dto.title,
                    description = dto.description,
                    category = category.lowercase()
                )
            }
            val savedData = visaDataService.saveVisaData(visaEntities)
            ResponseEntity.ok().body(CommonResponse(message = "Upload Successfully.",status = true))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(CommonResponse(message = e.message ?: "Something went wrong!",status = false))
        }
    }

}