package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.persistence.entities.VisaData

interface VisaDataService {
    fun saveVisaData(visaDataList: VisaData)
    fun getVisaDataByCategory(category: String): List<VisaData>
}