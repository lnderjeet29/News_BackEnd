package com.Inderjeet.News.business.gateway

interface CategoryService {
    fun getAllCategory():List<String>
    fun isCategoryNameExists(name: String): Boolean
    fun saveCategory(name: String)
}