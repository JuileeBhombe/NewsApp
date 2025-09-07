package com.juileebhombe.newsapp.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceTest {

    @Test
    fun `Resource Success should contain data and null message`() {
        // Given
        val testData = "Test Data"
        val resource = Resource.Success(testData)

        // When & Then
        assertTrue(resource is Resource.Success)
        assertEquals(testData, resource.data)
        assertNull(resource.message)
    }

    @Test
    fun `Resource Error should contain message and optional data`() {
        // Given
        val errorMessage = "Test Error Message"
        val testData = "Optional Data"
        val resourceWithData = Resource.Error<String>(errorMessage, testData)
        val resourceWithoutData = Resource.Error<String>(errorMessage)

        // When & Then
        assertTrue(resourceWithData is Resource.Error)
        assertEquals(errorMessage, resourceWithData.message)
        assertEquals(testData, resourceWithData.data)

        assertTrue(resourceWithoutData is Resource.Error)
        assertEquals(errorMessage, resourceWithoutData.message)
        assertNull(resourceWithoutData.data)
    }

    @Test
    fun `Resource Loading should contain optional data and null message`() {
        // Given
        val testData = "Loading Data"
        val resourceWithData = Resource.Loading(testData)
        val resourceWithoutData = Resource.Loading<String>()

        // When & Then
        assertTrue(resourceWithData is Resource.Loading)
        assertEquals(testData, resourceWithData.data)
        assertNull(resourceWithData.message)

        assertTrue(resourceWithoutData is Resource.Loading)
        assertNull(resourceWithoutData.data)
        assertNull(resourceWithoutData.message)
    }

    @Test
    fun `Resource Success should handle nullable data types`() {
        // Given
        val resource: Resource<String?> = Resource.Success(null)

        // When & Then
        assertTrue(resource is Resource.Success)
        assertNull(resource.data)
        assertNull(resource.message)
    }

    @Test
    fun `Resource Error should handle empty message`() {
        // Given
        val resource = Resource.Error<String>("")

        // When & Then
        assertTrue(resource is Resource.Error)
        assertEquals("", resource.message)
        assertNull(resource.data)
    }

    @Test
    fun `Resource Error should handle long error messages`() {
        // Given
        val longMessage = "This is a very long error message that might contain detailed information about what went wrong during the operation. It could include multiple sentences and technical details."
        val resource = Resource.Error<String>(longMessage)

        // When & Then
        assertTrue(resource is Resource.Error)
        assertEquals(longMessage, resource.message)
        assertNull(resource.data)
    }

    @Test
    fun `Resource Loading should work with different data types`() {
        // Given
        val stringResource = Resource.Loading("String Data")
        val intResource = Resource.Loading(42)
        val listResource = Resource.Loading(listOf("item1", "item2"))
        val nullResource = Resource.Loading<String>(null)

        // When & Then
        assertTrue(stringResource is Resource.Loading)
        assertEquals("String Data", stringResource.data)

        assertTrue(intResource is Resource.Loading)
        assertEquals(42, intResource.data)

        assertTrue(listResource is Resource.Loading)
        assertEquals(2, listResource.data?.size)

        assertTrue(nullResource is Resource.Loading)
        assertNull(nullResource.data)
    }

    @Test
    fun `Resource states should be properly distinguishable`() {
        // Given
        val success: Resource<String> = Resource.Success("data")
        val error: Resource<String> = Resource.Error("error")
        val loading: Resource<String> = Resource.Loading()

        // When & Then
        assertTrue(success is Resource.Success)
        assertTrue(error is Resource.Error)
        assertTrue(loading is Resource.Loading)

        // Ensure they are different types
        assertTrue(success !is Resource.Error)
        assertTrue(success !is Resource.Loading)
        assertTrue(error !is Resource.Success)
        assertTrue(error !is Resource.Loading)
        assertTrue(loading !is Resource.Success)
        assertTrue(loading !is Resource.Error)
    }

    @Test
    fun `Resource should handle complex data types`() {
        // Given
        data class TestData(val id: Int, val name: String)
        val complexData = TestData(1, "Test")
        val resource: Resource<TestData> = Resource.Success(complexData)

        // When & Then
        assertTrue(resource is Resource.Success)
        assertNotNull(resource.data)
        assertEquals(1, resource.data?.id)
        assertEquals("Test", resource.data?.name)
    }
}
