package com.codefylabs.Maple.Leaf.rest.controller

import com.codefylabs.Maple.Leaf.business.gateway.FAQService
import com.codefylabs.Maple.Leaf.persistence.entities.FAQ
import com.codefylabs.Maple.Leaf.persistence.repository.FAQRepository
import com.codefylabs.Maple.Leaf.rest.dto.CommonResponse
import com.codefylabs.Maple.Leaf.rest.dto.FAQDto
import com.codefylabs.Maple.Leaf.rest.dto.toDto
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class FAQController(val faqService: FAQService) {
    @GetMapping("/faq/subcategory/info")
    fun getFAQsByCategoryAndSubCategory(
        @RequestParam category: String,
        @RequestParam subCategory: String
    ): ResponseEntity<CommonResponse<FAQDto>> {
        return try {
            val faqs = faqService.getFAQsByCategoryAndSubCategory(category.trim().lowercase(), subCategory.trim().lowercase()).map { it.toDto() }
            ResponseEntity.ok(CommonResponse(message = "FAQs retrieved successfully", status = true, data = faqs[0]))
        } catch (e: Exception) {
            ResponseEntity.ok(CommonResponse(message = "Something went wrong.", status = false))
        }
    }

//    @GetMapping("/faq/all")
//    fun getAllFAQs(): ResponseEntity<CommonResponse<List<FAQDto>>> {
//        val faqs = faqService.getAllFAQs()
//        val response = faqs.map { it.toDto() }
//        return ResponseEntity.ok(CommonResponse(message = "All FAQs retrieved successfully", status = true, data = response))
//    }

    @PostMapping("/admin/create")
    fun createFAQ(@RequestBody request: FAQDto): ResponseEntity<CommonResponse<Boolean>> {
        return try {
            request.category=request.category.trim().lowercase()
            request.subCategory=request.subCategory.trim().lowercase()
            val createdFAQ = faqService.createFAQ(request)
            ResponseEntity.ok(CommonResponse(message = "FAQ created successfully", status = true, data = createdFAQ))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.internalServerError()
                .body(CommonResponse(status = false, message = "Failed to create FAQ", data = false))
        }
    }
}