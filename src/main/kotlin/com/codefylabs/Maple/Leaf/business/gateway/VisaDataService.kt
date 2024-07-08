package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.rest.dto.others.VisaDataDto

interface VisaDataService {
    fun saveVisaData(visaDataList: List<VisaData>)
    fun getVisaDataByCategory(category: String): List<VisaDataDto>
}