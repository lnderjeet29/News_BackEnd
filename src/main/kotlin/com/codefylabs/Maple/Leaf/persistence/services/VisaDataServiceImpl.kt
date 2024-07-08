package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.VisaDataService
import com.codefylabs.Maple.Leaf.persistence.entities.VisaData
import com.codefylabs.Maple.Leaf.persistence.repository.VisaDataRepository
import com.codefylabs.Maple.Leaf.rest.dto.others.VisaDataDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VisaDataServiceImpl(private val visaDataRepository: VisaDataRepository):VisaDataService {
    @Transactional
    override fun saveVisaData(visaDataList: List<VisaData>) {
        visaDataRepository.saveAll(visaDataList)
    }
    @Transactional(readOnly = true)
    override fun getVisaDataByCategory(category: String): List<VisaDataDto> {
        return visaDataRepository.findByCategory(category).map { visaData ->
            VisaDataDto(
                title = visaData.title,
                description = visaData.description
            )
        }
    }
}