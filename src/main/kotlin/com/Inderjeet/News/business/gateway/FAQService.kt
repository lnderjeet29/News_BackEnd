package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.FAQ
import com.Inderjeet.News.rest.dto.FAQDto

interface FAQService {

    fun createFAQ(createFAQDto: FAQDto):Boolean

    fun getFAQsByCategoryAndSubCategory(category: String, subCategory: String): List<FAQ>

}