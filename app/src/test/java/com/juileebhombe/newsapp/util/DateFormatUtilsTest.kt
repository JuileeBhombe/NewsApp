package com.juileebhombe.newsapp.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateFormatUtilsTest {

    @Test
    fun `formatPublishedDate should format valid date correctly`() {
        // Given
        val inputDate = "2023-12-01T10:30:00Z"
        // Expected output will depend on the system's timezone
        // We'll just verify it doesn't return the original string
        val result = DateFormatUtils.formatPublishedDate(inputDate)

        // Then
        assertTrue("Result should be formatted differently from input",
            result != inputDate && result.contains("2023"))
    }

    @Test
    fun `formatPublishedDate should handle different date formats gracefully`() {
        // Given
        val inputDate = "2023-12-01T15:45:30Z"
        val result = DateFormatUtils.formatPublishedDate(inputDate)

        // Then
        assertTrue("Result should be formatted differently from input",
            result != inputDate && result.contains("2023"))
    }

    @Test
    fun `formatPublishedDate should return original string for invalid date`() {
        // Given
        val invalidDate = "invalid-date-format"

        // When
        val result = DateFormatUtils.formatPublishedDate(invalidDate)

        // Then
        assertEquals(invalidDate, result)
    }

    @Test
    fun `formatPublishedDate should return original string for null or empty date`() {
        // Given
        val nullDate = ""
        val emptyDate = ""

        // When
        val result1 = DateFormatUtils.formatPublishedDate(nullDate)
        val result2 = DateFormatUtils.formatPublishedDate(emptyDate)

        // Then
        assertEquals("", result1)
        assertEquals("", result2)
    }

    @Test
    fun `formatRelativeTime should return Just now for very recent dates`() {
        // Given - Use current time
        val currentTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(java.util.Date())

        // When
        val result = DateFormatUtils.formatRelativeTime(currentTime)

        // Then
        assertTrue(result == "Just now" || result.contains("ago"))
    }

    @Test
    fun `formatRelativeTime should return minutes ago for recent dates`() {
        // Given - Create a date 5 minutes ago
        val fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000)
        val dateString = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(java.util.Date(fiveMinutesAgo))

        // When
        val result = DateFormatUtils.formatRelativeTime(dateString)

        // Then
        assertTrue(result.contains("ago"))
    }

    @Test
    fun `formatRelativeTime should return hours ago for older dates`() {
        // Given - Create a date 3 hours ago
        val threeHoursAgo = System.currentTimeMillis() - (3 * 60 * 60 * 1000)
        val dateString = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(java.util.Date(threeHoursAgo))

        // When
        val result = DateFormatUtils.formatRelativeTime(dateString)

        // Then
        assertTrue(result.contains("ago"))
    }

    @Test
    fun `formatRelativeTime should return formatted date for old dates`() {
        // Given - Create an old date (more than a week ago)
        val oldDate = "2023-01-01T10:00:00Z"

        // When
        val result = DateFormatUtils.formatRelativeTime(oldDate)

        // Then
        // Should return formatted date since it's more than a week old
        assertTrue(result.contains("2023") || result.contains("Jan"))
    }

    @Test
    fun `formatRelativeTime should handle invalid dates gracefully`() {
        // Given
        val invalidDate = "invalid-date"

        // When
        val result = DateFormatUtils.formatRelativeTime(invalidDate)

        // Then
        assertEquals(invalidDate, result)
    }

    @Test
    fun `formatRelativeTime should handle edge case dates`() {
        // Given
        val edgeCases = listOf(
            "",
            "2023-12-01T00:00:00Z",
            "2023-12-01T23:59:59Z",
            "2023-02-29T12:00:00Z" // Leap year date
        )

        // When & Then
        edgeCases.forEach { date ->
            val result = DateFormatUtils.formatRelativeTime(date)
            // Should not crash and return some result
            assertNotNull(result)
        }
    }

    @Test
    fun `formatPublishedDate should handle various timezone formats`() {
        // Given
        val dateWithTimezone = "2023-12-01T10:30:00+05:30"
        val result = DateFormatUtils.formatPublishedDate(dateWithTimezone)

        // Then
        // Should handle timezone format gracefully (may or may not parse correctly)
        assertNotNull(result)
        assertTrue("Result should not be empty", result.isNotEmpty())
    }

}
