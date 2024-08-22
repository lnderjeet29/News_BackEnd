package com.Inderjeet.News.rest.dto;

data class CommonResponse<T>(
    val message: String,
    val status: Boolean,
    val data: T?=null
)
