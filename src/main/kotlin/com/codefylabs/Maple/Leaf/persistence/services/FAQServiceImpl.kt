package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.FAQService
import com.codefylabs.Maple.Leaf.persistence.entities.FAQ
import com.codefylabs.Maple.Leaf.persistence.entities.SubFAQ
import com.codefylabs.Maple.Leaf.persistence.repository.FAQRepository
import com.codefylabs.Maple.Leaf.rest.dto.FAQDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FAQServiceImpl(private val faqRepository: FAQRepository):FAQService {
    @Transactional
    override fun createFAQ(createFAQDto: FAQDto):Boolean {
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