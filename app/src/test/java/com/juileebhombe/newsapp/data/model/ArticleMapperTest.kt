package com.juileebhombe.newsapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ArticleMapperTest {

    @Test
    fun `ArticleDto toDomain should map all fields correctly`() {
        // Given
        val sourceDto = SourceDto(id = "test-id", name = "Test Source")
        val articleDto = ArticleDto(
            source = sourceDto,
            author = "Test Author",
            title = "Test Title",
            description = "Test Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2023-12-01T10:00:00Z",
            content = "Test Content"
        )

        // When
        val result = articleDto.toDomain()

        // Then
        assertNotNull(result)
        assertEquals("Test Title", result.title)
        assertEquals("Test Author", result.author)
        assertEquals("Test Description", result.description)
        assertEquals("https://example.com", result.url)
        assertEquals("https://example.com/image.jpg", result.urlToImage)
        assertEquals("2023-12-01T10:00:00Z", result.publishedAt)
        assertEquals("Test Content", result.content)
        assertNotNull(result.source)
        assertEquals("test-id", result.source?.id)
        assertEquals("Test Source", result.source?.name)
    }

    @Test
    fun `ArticleDto toDomain should handle null source`() {
        // Given
        val articleDto = ArticleDto(
            source = null,
            author = "Test Author",
            title = "Test Title",
            description = "Test Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2023-12-01T10:00:00Z",
            content = "Test Content"
        )

        // When
        val result = articleDto.toDomain()

        // Then
        assertNotNull(result)
        assertEquals("Test Title", result.title)
        assertEquals(null, result.source)
    }

    @Test
    fun `SourceDto toDomain should map all fields correctly`() {
        // Given
        val sourceDto = SourceDto(id = "test-id", name = "Test Source")

        // When
        val result = sourceDto.toDomain()

        // Then
        assertNotNull(result)
        assertEquals("test-id", result.id)
        assertEquals("Test Source", result.name)
    }

    @Test
    fun `SourceDto toDomain should handle null id`() {
        // Given
        val sourceDto = SourceDto(id = null, name = "Test Source")

        // When
        val result = sourceDto.toDomain()

        // Then
        assertNotNull(result)
        assertEquals(null, result.id)
        assertEquals("Test Source", result.name)
    }

    @Test
    fun `ArticleResponse toDomain should map all articles correctly`() {
        // Given
        val sourceDto = SourceDto(id = "test-id", name = "Test Source")
        val articleDto = ArticleDto(
            source = sourceDto,
            author = "Test Author",
            title = "Test Title",
            description = "Test Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2023-12-01T10:00:00Z",
            content = "Test Content"
        )
        val articleResponse = ArticleResponse(
            status = "ok",
            totalResults = 1,
            articles = listOf(articleDto)
        )

        // When
        val result = articleResponse.toDomain()

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Test Title", result[0].title)
        assertEquals("Test Author", result[0].author)
        assertEquals("Test Description", result[0].description)
    }

    @Test
    fun `ArticleResponse toDomain should handle empty articles list`() {
        // Given
        val articleResponse = ArticleResponse(
            status = "ok",
            totalResults = 0,
            articles = emptyList()
        )

        // When
        val result = articleResponse.toDomain()

        // Then
        assertNotNull(result)
        assertEquals(0, result.size)
    }

    @Test
    fun `ArticleDto toDomain should handle null optional fields`() {
        // Given
        val articleDto = ArticleDto(
            source = null,
            author = null,
            title = "Test Title",
            description = null,
            url = "https://example.com",
            urlToImage = null,
            publishedAt = "2023-12-01T10:00:00Z",
            content = null
        )

        // When
        val result = articleDto.toDomain()

        // Then
        assertNotNull(result)
        assertEquals("Test Title", result.title)
        assertEquals("https://example.com", result.url)
        assertEquals("2023-12-01T10:00:00Z", result.publishedAt)
        assertEquals(null, result.author)
        assertEquals(null, result.description)
        assertEquals(null, result.urlToImage)
        assertEquals(null, result.content)
        assertEquals(null, result.source)
    }
}
