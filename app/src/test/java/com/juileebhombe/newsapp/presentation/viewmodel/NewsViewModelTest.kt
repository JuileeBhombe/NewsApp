package com.juileebhombe.newsapp.presentation.viewmodel

import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.domain.usecase.GetTopHeadlinesUseCase
import com.juileebhombe.newsapp.util.ConnectivityObserver
import com.juileebhombe.newsapp.util.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsViewModelTest {

    private lateinit var getTopHeadlinesUseCase: GetTopHeadlinesUseCase
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var newsViewModel: NewsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTopHeadlinesUseCase = mockk()
        connectivityObserver = mockk()

        // Set up default connectivity observer to return connected state
        every { connectivityObserver.isConnected } returns flowOf(true)

        // Mock the initial loadNews call that happens in init block
        coEvery { getTopHeadlinesUseCase(page = 1, pageSize = 10) } returns Resource.Loading()

        newsViewModel = NewsViewModel(getTopHeadlinesUseCase, connectivityObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadNews should emit success state when use case returns success`() = runTest {
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
        val successResource = Resource.Success(mockArticles)

        // Clear previous mock and set up new expectation
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns successResource

        // When
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Success)
        val successState = state as NewsViewModel.NewsState.Success
        assertEquals(mockArticles, successState.articles)
    }

    @Test
    fun `loadNews should emit error state when use case returns error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val errorResource = Resource.Error<List<Article>>(errorMessage)

        // Clear previous mock and set up new expectation
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns errorResource

        // When
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Error)
        val errorState = state as NewsViewModel.NewsState.Error
        assertEquals(errorMessage, errorState.message)
    }

    @Test
    fun `loadNews should emit error state when not connected to internet`() = runTest {
        // Given - Create a new ViewModel with offline connectivity
        val offlineConnectivityObserver = mockk<ConnectivityObserver>()
        every {
            offlineConnectivityObserver.isConnected
        } returns flowOf(false)

        val offlineViewModel = NewsViewModel(getTopHeadlinesUseCase, offlineConnectivityObserver)

        // When
        offlineViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = offlineViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Error)
        val errorState = state as NewsViewModel.NewsState.Error
        assertTrue(errorState.message?.contains("No internet connection") == true)
    }

    @Test
    fun `loadMoreNews should append new articles to existing list when successful`() = runTest {
        // Given
        val initialArticles = listOf(
            Article(
                title = "Initial Article",
                description = "Initial Description",
                url = "https://example.com/1",
                publishedAt = "2023-12-01T10:00:00Z"
            )
        )
        val newArticles = listOf(
            Article(
                title = "New Article",
                description = "New Description",
                url = "https://example.com/2",
                publishedAt = "2023-12-02T10:00:00Z"
            )
        )

        // Clear previous mocks and set up new expectations
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns Resource.Success(initialArticles)

        // Load initial data
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Clear mock again and set up for load more
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 2, pageSize = 10)
        } returns Resource.Success(newArticles)

        // When
        newsViewModel.loadMoreNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Success)
        val successState = state as NewsViewModel.NewsState.Success
        assertEquals(2, successState.articles.size)
        assertEquals("Initial Article", successState.articles[0].title)
        assertEquals("New Article", successState.articles[1].title)
    }

    @Test
    fun `loadMoreNews should handle empty response and stop pagination`() = runTest {
        // Given
        val initialArticles = listOf(
            Article(
                title = "Initial Article",
                description = "Initial Description",
                url = "https://example.com/1",
                publishedAt = "2023-12-01T10:00:00Z"
            )
        )
        val emptyResponse = emptyList<Article>()

        // Clear previous mocks and set up new expectations
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns Resource.Success(initialArticles)

        // Load initial data
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Clear mock again and set up for load more with empty response
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 2, pageSize = 10)
        } returns Resource.Success(emptyResponse)

        // When - Try to load more
        newsViewModel.loadMoreNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should maintain original list
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Success)
        val successState = state as NewsViewModel.NewsState.Success
        assertEquals(1, successState.articles.size)
        assertEquals("Initial Article", successState.articles[0].title)
    }

    @Test
    fun `loadMoreNews should handle network error gracefully`() = runTest {
        // Given
        val initialArticles = listOf(
            Article(
                title = "Initial Article",
                description = "Initial Description",
                url = "https://example.com/1",
                publishedAt = "2023-12-01T10:00:00Z"
            )
        )

        // Clear previous mocks and set up new expectations
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns Resource.Success(initialArticles)

        // Load initial data
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Clear mock again and set up for load more with error
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 2, pageSize = 10)
        } returns Resource.Error("Network error")

        // When - Try to load more (should fail)
        newsViewModel.loadMoreNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should maintain original list
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Success)
        val successState = state as NewsViewModel.NewsState.Success
        assertEquals(1, successState.articles.size)
        assertEquals("Initial Article", successState.articles[0].title)
    }


    @Test
    fun `retry should reload data successfully`() = runTest {
        // Given
        val articles = listOf(
            Article(
                title = "Retry Article",
                description = "Test Description",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z"
            )
        )

        // Clear previous mocks and set up new expectations
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns Resource.Success(articles)

        // When
        newsViewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Success)
        val successState = state as NewsViewModel.NewsState.Success
        assertEquals(1, successState.articles.size)
        assertEquals("Retry Article", successState.articles[0].title)
    }

    @Test
    fun `initial load should handle empty article list correctly`() = runTest {
        // Given
        val emptyArticles = emptyList<Article>()

        // Clear previous mocks and set up new expectations
        clearMocks(getTopHeadlinesUseCase)
        coEvery {
            getTopHeadlinesUseCase(page = 1, pageSize = 10)
        } returns Resource.Success(emptyArticles)

        // When
        newsViewModel.loadNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = newsViewModel.newsState.value
        assertTrue(state is NewsViewModel.NewsState.Empty)
    }

    @Test
    fun `NewsState sealed class should handle all states correctly`() {
        // Test Success state
        val successState = NewsViewModel.NewsState.Success(emptyList())
        assertTrue(successState is NewsViewModel.NewsState.Success)

        // Test Error state
        val errorState = NewsViewModel.NewsState.Error("Test error")
        assertTrue(errorState is NewsViewModel.NewsState.Error)
        assertEquals("Test error", errorState.message)

        // Test Loading state
        val loadingState = NewsViewModel.NewsState.Loading
        assertTrue(loadingState is NewsViewModel.NewsState.Loading)

        // Test Empty state
        val emptyState = NewsViewModel.NewsState.Empty
        assertTrue(emptyState is NewsViewModel.NewsState.Empty)
    }
}
