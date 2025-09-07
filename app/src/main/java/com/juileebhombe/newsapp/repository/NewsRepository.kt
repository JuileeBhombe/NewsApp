package com.juileebhombe.newsapp.repository

import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.util.Resource

interface NewsRepository {
    suspend fun getTopHeadlines(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<List<Article>>
}
