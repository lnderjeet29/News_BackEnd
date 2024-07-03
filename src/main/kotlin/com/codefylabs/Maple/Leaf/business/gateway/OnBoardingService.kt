package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.user.OnBoardingDto

interface OnBoardingService {
    fun saveSurveyAnswers(userId: Int, questionsAndAnswers: List<OnBoardingDto>)
}