package com.codefylabs.Maple.Leaf.persistence.services

import com.codefylabs.Maple.Leaf.business.gateway.CategoryService
import com.codefylabs.Maple.Leaf.persistence.entities.news.Category
import com.codefylabs.Maple.Leaf.persistence.repository.CategoryRepository
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