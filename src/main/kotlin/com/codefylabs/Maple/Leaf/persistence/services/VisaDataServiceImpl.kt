package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.VisaDataService
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.persistence.repository.VisaDataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VisaDataServiceImpl(private val visaDataRepository: VisaDataRepository) : VisaDataService {
    @Transactional
    override fun saveVisaData(visaDataList: VisaData) {
        visaDataRepository.save(visaDataList)
    }

    @Transactional(readOnly = true)
    override fun getVisaDataByCategory(category: String): List<VisaData> {
        return visaDataRepository.findByKey(category)
    }
}