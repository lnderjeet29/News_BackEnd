package com.codefylabs.Maple.Leaf.rest.dto;

data class CommonResponse<T>(
    val message: String,
    val status: Boolean,
    val data: T?=null
)
