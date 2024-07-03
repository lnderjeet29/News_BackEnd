package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.OnBoardingService
import com.codefylabs.Maple.Leaf.persistence.entities.OnBoardingData
import com.codefylabs.Maple.Leaf.persistence.repository.OnBoardingRepository
import com.codefylabs.Maple.Leaf.rest.dto.user.OnBoardingDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OnBoardingServiceImpl(val onBoardingRepository: OnBoardingRepository) : OnBoardingService{

    @Transactional
    override fun saveSurveyAnswers(userId: Int, questionsAndAnswers: List<OnBoardingDto>) {
        val surveyAnswers = questionsAndAnswers.map {
            OnBoardingData(userId = userId, question = it.question, answer = it.answer)
        }
        onBoardingRepository.saveAll(surveyAnswers)

    }

}