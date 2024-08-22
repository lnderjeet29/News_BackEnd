package com.Inderjeet.News.persistence.services

import com.Inderjeet.News.business.gateway.FAQService
import com.Inderjeet.News.persistence.entities.FAQ
import com.Inderjeet.News.persistence.entities.SubFAQ
import com.Inderjeet.News.persistence.repository.FAQRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FAQServiceImpl(private val faqRepository: FAQRepository): FAQService {
    @Transactional
    override fun createFAQ(createFAQDto: com.Inderjeet.News.rest.dto.FAQDto):Boolean {
        val faq = FAQ(
            category = createFAQDto.category,
            subCategory = createFAQDto.subCategory,
            title = createFAQDto.title,
            content = createFAQDto.content,
            subFaqs = createFAQDto.faqs.map { SubFAQ(it.question, it.answer) }
        )
        faqRepository.save(faq)
        return true
    }
//    @Transactional(readOnly = true)
//    override fun getAllFAQs(): List<FAQ> = faqRepository.findAllWithSubFaqs()

    @Transactional(readOnly = true)
    override fun getFAQsByCategoryAndSubCategory(category: String, subCategory: String): List<FAQ> =
    faqRepository.findByCategoryAndSubCategoryWithSubFaqs(category, subCategory)
}