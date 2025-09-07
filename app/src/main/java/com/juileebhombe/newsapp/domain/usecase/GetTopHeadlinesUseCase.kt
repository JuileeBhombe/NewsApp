package com.juileebhombe.newsapp.domain.usecase

import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.repository.NewsRepository
import com.juileebhombe.newsapp.util.Resource
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<List<Article>> {
        return newsRepository.getTopHeadlines(
            country = country,
            page = page,
            pageSize = pageSize
        )
    }
}
