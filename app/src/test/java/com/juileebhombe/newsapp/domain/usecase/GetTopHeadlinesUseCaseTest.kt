package com.juileebhombe.newsapp.domain.usecase

import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.repository.NewsRepository
import com.juileebhombe.newsapp.util.Resource
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTopHeadlinesUseCaseTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var getTopHeadlinesUseCase: GetTopHeadlinesUseCase

    @Before
    fun setUp() {
        newsRepository = mockk()
        getTopHeadlinesUseCase = GetTopHeadlinesUseCase(newsRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val mockArticles = listOf(
            Article(
                title = "Test Article",
                description = "Test Description",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z"
            )
        )
        val expectedResource = Resource.Success(mockArticles)

        coEvery {
            newsRepository.getTopHeadlines(country = "us", page = 1, pageSize = 20)
        } returns expectedResource

        // When
        val result = getTopHeadlinesUseCase(country = "us", page = 1, pageSize = 20)

        // Then
        assertEquals(expectedResource, result)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val expectedResource = Resource.Error<List<Article>>(errorMessage)

        coEvery {
            newsRepository.getTopHeadlines(country = "us", page = 1, pageSize = 20)
        } returns expectedResource

        // When
        val result = getTopHeadlinesUseCase(country = "us", page = 1, pageSize = 20)

        // Then
        assertEquals(expectedResource, result)
    }

    @Test
    fun `invoke should use default parameters when not provided`() = runTest {
        // Given
        val mockArticles = emptyList<Article>()
        val expectedResource = Resource.Success(mockArticles)

        coEvery {
            newsRepository.getTopHeadlines(country = "us", page = 1, pageSize = 20)
        } returns expectedResource

        // When
        val result = getTopHeadlinesUseCase()

        // Then
        assertEquals(expectedResource, result)
    }
}
