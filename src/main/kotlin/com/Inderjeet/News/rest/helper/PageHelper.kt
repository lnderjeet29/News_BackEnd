package com.Inderjeet.News.rest.helper

import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page


object PageHelper {
        fun <U, V> getPageResponse(page: Page<U>, type: Class<V>): com.Inderjeet.News.rest.dto.PaginatedResponse<V> {
            val list1 = page.content
            val list = list1.map { obj -> ModelMapper().map(obj, type) }
            return com.Inderjeet.News.rest.dto.PaginatedResponse<V>(
                content = list,
                pageNumber = page.number,
                totalElements = page.totalElements,
                pageSize = page.size,
                isLastPage = page.isLast,
                totalPages = page.totalPages
            )
        }

    }
