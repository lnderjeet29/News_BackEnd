package com.Inderjeet.News.persistence.services

import com.Inderjeet.News.business.gateway.CategoryService
import com.Inderjeet.News.persistence.repository.CategoryRepository
import com.Inderjeet.News.persistence.entities.news.Category
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(val categoryRepository: CategoryRepository) :CategoryService{
    override fun getAllCategory(): List<String> {
        val categories = categoryRepository.findAll()
        return categories.map { it.name }
    }
    override fun isCategoryNameExists(name: String): Boolean {
        return categoryRepository.existsByName(name)
    }

    override fun saveCategory(name: String) {
        val value =Category(name = name)
        categoryRepository.save(value)
    }
}