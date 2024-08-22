package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.FAQ
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FAQRepository : JpaRepository<FAQ, Int> {

    @Query("SELECT f FROM FAQ f LEFT JOIN FETCH f.subFaqs WHERE f.category = :category AND f.subCategory = :subCategory")
    fun findByCategoryAndSubCategoryWithSubFaqs(category: String, subCategory: String): List<FAQ>
}