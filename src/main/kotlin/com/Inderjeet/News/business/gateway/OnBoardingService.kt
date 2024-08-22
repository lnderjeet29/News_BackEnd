package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.rest.dto.user.OnBoardingDto

interface OnBoardingService {
    fun saveSurveyAnswers(userId: Int, questionsAndAnswers: List<OnBoardingDto>)
}