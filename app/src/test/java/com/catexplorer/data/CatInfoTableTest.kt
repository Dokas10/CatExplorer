package com.catexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CatInfoTableTest {

    @Test
    fun testInitialization() {
        val catInfoTable = CatInfoTable(
            id = "1",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            breedId = "persian",
            lifespan = "15-20 years",
            name = "Fluffy",
            origin = "Iran",
            temperament = "Calm",
            description = "A fluffy cat",
            isFavorite = true
        )

        assertEquals("1", catInfoTable.id)
        assertEquals("https://example.com/cat.jpg", catInfoTable.url)
        assertEquals(800, catInfoTable.width)
        assertEquals(600, catInfoTable.height)
        assertEquals("persian", catInfoTable.breedId)
        assertEquals("15-20 years", catInfoTable.lifespan)
        assertEquals("Fluffy", catInfoTable.name)
        assertEquals("Iran", catInfoTable.origin)
        assertEquals("Calm", catInfoTable.temperament)
        assertEquals("A fluffy cat", catInfoTable.description)
        assertEquals(true, catInfoTable.isFavorite)
    }

    @Test
    fun testInitializationWithNullValues() {
        val catInfoTable = CatInfoTable(
            id = "1",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            breedId = "persian",
            lifespan = "15-20 years",
            name = "Fluffy",
            origin = "Iran",
            temperament = "Calm",
            description = "A fluffy cat",
            isFavorite = false
        )

        assertEquals("1", catInfoTable.id)
        assertEquals("https://example.com/cat.jpg", catInfoTable.url)
        assertEquals(800, catInfoTable.width)
        assertEquals(600, catInfoTable.height)
        assertEquals("persian", catInfoTable.breedId)
        assertEquals("15-20 years", catInfoTable.lifespan)
        assertEquals("Fluffy", catInfoTable.name)
        assertEquals("Iran", catInfoTable.origin)
        assertEquals("Calm", catInfoTable.temperament)
        assertEquals("A fluffy cat", catInfoTable.description)
        assertEquals(false, catInfoTable.isFavorite)
    }

    @Test
    fun testEquality() {
        val catInfoTable1 = CatInfoTable(
            id = "1",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            breedId = "persian",
            lifespan = "15-20 years",
            name = "Fluffy",
            origin = "Iran",
            temperament = "Calm",
            description = "A fluffy cat",
            isFavorite = true
        )

        val catInfoTable2 = CatInfoTable(
            id = "1",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            breedId = "persian",
            lifespan = "15-20 years",
            name = "Fluffy",
            origin = "Iran",
            temperament = "Calm",
            description = "A fluffy cat",
            isFavorite = true
        )

        assertEquals(catInfoTable1, catInfoTable2)
    }

    @Test
    fun testInequality() {
        val catInfoTable1 = CatInfoTable(
            id = "1",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            breedId = "persian",
            lifespan = "15-20 years",
            name = "Fluffy",
            origin = "Iran",
            temperament = "Calm",
            description = "A fluffy cat",
            isFavorite = true
        )

        val catInfoTable2 = CatInfoTable(
            id = "2",
            url = "https://example.com/cat2.jpg",
            width = 700,
            height = 500,
            breedId = "siamese",
            lifespan = "12-15 years",
            name = "Mittens",
            origin = "Thailand",
            temperament = "Playful",
            description = "A playful cat",
            isFavorite = false
        )

        assertNotEquals(catInfoTable1, catInfoTable2)
    }
}