package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.FAQ
import com.codefylabs.Maple.Leaf.rest.dto.FAQDto

interface FAQService {

    fun createFAQ(createFAQDto: FAQDto):Boolean

//    fun getAllFAQs(): List<FAQ>

    fun getFAQsByCategoryAndSubCategory(category: String, subCategory: String): List<FAQ>

}