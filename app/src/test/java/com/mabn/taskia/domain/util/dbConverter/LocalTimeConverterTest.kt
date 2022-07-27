package com.mabn.taskia.domain.util.dbConverter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalTime

class LocalTimeConverterTest {

    @Test
    fun `convert string to LocalTime`() {
        val calculatedTime = LocalTimeConverter.stringToLocalTime("10:50")
        val expectedTime = LocalTime.of(10, 50)
        assertEquals(calculatedTime, expectedTime)
    }

    @Test
    fun `convert no time label to LocalTime (expected null)`() {
        assertNull(LocalTimeConverter.stringToLocalTime("--:--"))
        assertNull(LocalTimeConverter.stringToLocalTime(""))
    }
}