package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.VisaData

interface VisaDataService {
    fun saveVisaData(visaDataList: VisaData)
    fun getVisaDataByCategory(category: String): List<VisaData>
}