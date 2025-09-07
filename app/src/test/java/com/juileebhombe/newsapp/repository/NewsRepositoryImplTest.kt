package com.juileebhombe.newsapp.repository

import com.juileebhombe.newsapp.data.api.NewsApiService
import com.juileebhombe.newsapp.data.model.ArticleDto
import com.juileebhombe.newsapp.data.model.ArticleResponse
import com.juileebhombe.newsapp.data.model.SourceDto
import com.juileebhombe.newsapp.util.Resource
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NewsRepositoryImplTest {

    private lateinit var newsApiService: NewsApiService
    private lateinit var newsRepositoryImpl: NewsRepositoryImpl

    @Before
    fun setUp() {
        newsApiService = mockk()
        newsRepositoryImpl = NewsRepositoryImpl(newsApiService)
    }

    @Test
    fun `getTopHeadlines should return success when API call is successful`() = runTest {
        // Given
        val mockArticleResponse = ArticleResponse(
            status = "ok",
            totalResults = 1,
            articles = listOf(
                ArticleDto(
                    source = SourceDto(id = "test-source", name = "Test Source"),
                    author = "Test Author",
                    title = "Test Title",
                    description = "Test Description",
                    url = "https://example.com",
                    urlToImage = "https://example.com/image.jpg",
                    publishedAt = "2023-12-01T10:00:00Z",
                    content = "Test Content"
                )
            )
        )

        val mockResponse = Response.success(mockArticleResponse)

        coEvery {
            newsApiService.getTopHeadlines(
                country = "us",
                apiKey = any<String>(),
                page = 1,
                pageSize = 20
            )
        } returns mockResponse

        // When
        val result = newsRepositoryImpl.getTopHeadlines(country = "us", page = 1, pageSize = 20)

        // Then
        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        assertEquals(1, successResult.data?.size)
        assertEquals("Test Title", successResult.data?.first()?.title)
    }

    @Test
    fun `getTopHeadlines should return error when API call fails with HTTP error`() = runTest {
        // Given
        val errorResponse = Response.error<ArticleResponse>(
            404,
            "".toResponseBody()
        )

        coEvery {
            newsApiService.getTopHeadlines(
                country = "us",
                apiKey = any<String>(),
                page = 1,
                pageSize = 20
            )
        } returns errorResponse

        // When
        val result = newsRepositoryImpl.getTopHeadlines(country = "us", page = 1, pageSize = 20)

        // Then
        assertTrue(result is Resource.Error)
        val errorResult = result as Resource.Error
        assertTrue(errorResult.message?.contains("API Error") == true)
    }

    @Test
    fun `getTopHeadlines should return error when network exception occurs`() = runTest {
        // Given
        coEvery {
            newsApiService.getTopHeadlines(
                country = "us",
                apiKey = any<String>(),
                page = 1,
                pageSize = 20
            )
        } throws Exception("Network error")

        // When
        val result = newsRepositoryImpl.getTopHeadlines(country = "us", page = 1, pageSize = 20)

        // Then
        assertTrue(result is Resource.Error)
        val errorResult = result as Resource.Error
        assertTrue(errorResult.message?.contains("Unexpected error") == true)
    }
}
