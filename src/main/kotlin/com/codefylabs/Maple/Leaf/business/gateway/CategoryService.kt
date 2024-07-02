package com.codefylabs.Maple.Leaf.business.gateway

interface CategoryService {
    fun getAllCategory():List<String>
    fun isCategoryNameExists(name: String): Boolean
    fun saveCategory(name: String)
}