package com.Inderjeet.News.persistence.repository

import com.Inderjeet.News.persistence.entities.OnBoardingData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OnBoardingRepository: JpaRepository<OnBoardingData,Int>{
}