package com.catexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CatBreedInfoTest {
    @Test
    fun testInitialization() {
        val catBreedInfo = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = "Calm",
            origin = "Iran",
            description = "Long-haired cat breed",
            life_span = "15-20 years"
        )

        assertEquals("1", catBreedInfo.id)
        assertEquals("Persian", catBreedInfo.name)
        assertEquals("Calm", catBreedInfo.temperament)
        assertEquals("Iran", catBreedInfo.origin)
        assertEquals("Long-haired cat breed", catBreedInfo.description)
        assertEquals("15-20 years", catBreedInfo.life_span)
    }

    @Test
    fun testEquality() {
        val catBreedInfo1 = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = "Calm",
            origin = "Iran",
            description = "Long-haired cat breed",
            life_span = "15-20 years"
        )

        val catBreedInfo2 = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = "Calm",
            origin = "Iran",
            description = "Long-haired cat breed",
            life_span = "15-20 years"
        )

        assertEquals(catBreedInfo1, catBreedInfo2)
    }

    @Test
    fun testInitializationWithNullValues() {
        val catBreedInfo = CatBreedInfo(
            id = null,
            name = null,
            temperament = null,
            origin = null,
            description = null,
            life_span = null
        )

        assertEquals(null, catBreedInfo.id)
        assertEquals(null, catBreedInfo.name)
        assertEquals(null, catBreedInfo.temperament)
        assertEquals(null, catBreedInfo.origin)
        assertEquals(null, catBreedInfo.description)
        assertEquals(null, catBreedInfo.life_span)
    }

    @Test
    fun testEqualityWithNullValues() {
        val catBreedInfo1 = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = null,
            origin = null,
            description = null,
            life_span = null
        )

        val catBreedInfo2 = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = null,
            origin = null,
            description = null,
            life_span = null
        )

        assertEquals(catBreedInfo1, catBreedInfo2)
    }

    @Test
    fun testInequality() {
        val catBreedInfo1 = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = "Calm",
            origin = "Iran",
            description = "Long-haired cat breed",
            life_span = "15-20 years"
        )

        val catBreedInfo2 = CatBreedInfo(
            id = "2",
            name = "Siamese",
            temperament = "Playful",
            origin = "Thailand",
            description = "Short-haired cat breed",
            life_span = "12-15 years"
        )

        assertNotEquals(catBreedInfo1, catBreedInfo2)
    }
}