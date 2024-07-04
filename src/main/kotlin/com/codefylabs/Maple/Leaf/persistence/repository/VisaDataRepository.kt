package com.codefylabs.Maple.Leaf.persistence.repository

import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VisaDataRepository : JpaRepository<VisaData,Int> {
    fun findByCategory(category: String): List<VisaData>

    @Query("SELECT DISTINCT v.category FROM VisaData v")
    fun findDistinctCategories(): List<String>
}