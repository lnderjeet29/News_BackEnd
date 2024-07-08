package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.FAQ
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FAQRepository : JpaRepository<FAQ, Int> {

//    @Query("SELECT f FROM FAQ f LEFT JOIN FETCH f.subFaqs")
//    fun findAllWithSubFaqs(): List<FAQ>

    @Query("SELECT f FROM FAQ f LEFT JOIN FETCH f.subFaqs WHERE f.category = :category AND f.subCategory = :subCategory")
    fun findByCategoryAndSubCategoryWithSubFaqs(category: String, subCategory: String): List<FAQ>
}