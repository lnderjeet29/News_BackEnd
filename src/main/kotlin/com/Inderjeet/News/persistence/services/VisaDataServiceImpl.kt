package com.Inderjeet.News.persistence.services

import com.Inderjeet.News.business.gateway.VisaDataService
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.persistence.repository.VisaDataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VisaDataServiceImpl(private val visaDataRepository: VisaDataRepository) :
    com.Inderjeet.News.business.gateway.VisaDataService {
    @Transactional
    override fun saveVisaData(visaDataList: VisaData) {
        visaDataRepository.save(visaDataList)
    }

    @Transactional(readOnly = true)
    override fun getVisaDataByCategory(category: String): List<VisaData> {
        return visaDataRepository.findByKey(category)
    }
}