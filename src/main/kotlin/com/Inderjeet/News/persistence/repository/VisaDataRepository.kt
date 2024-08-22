package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.VisaData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VisaDataRepository : JpaRepository<VisaData,Int> {
    fun findByKey(key: String): List<VisaData>

}