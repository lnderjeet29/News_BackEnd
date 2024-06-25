package com.codefylabs.Maple.Leaf.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface NewsRepositoryJPA: JpaRepository<News, String> {

    fun findByNewsId(newsId: String): Optional<News>

//    fun findByIsTrending():
}