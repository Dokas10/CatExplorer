package com.catexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CatMainInfoTest {

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

        val catMainInfo = CatMainInfo(
            id = "123",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            catBreedInfo = listOf(catBreedInfo),
            favorite = true
        )

        assertEquals("123", catMainInfo.id)
        assertEquals("https://example.com/cat.jpg", catMainInfo.url)
        assertEquals(800, catMainInfo.width)
        assertEquals(600, catMainInfo.height)
        assertEquals(listOf(catBreedInfo), catMainInfo.catBreedInfo)
        assertEquals(true, catMainInfo.favorite)
    }

    @Test
    fun testEquality() {
        val catBreedInfo = CatBreedInfo(
            id = "1",
            name = "Persian",
            temperament = "Calm",
            origin = "Iran",
            description = "Long-haired cat breed",
            life_span = "15-20 years"
        )

        val catMainInfo1 = CatMainInfo(
            id = "123",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            catBreedInfo = listOf(catBreedInfo),
            favorite = true
        )

        val catMainInfo2 = CatMainInfo(
            id = "123",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            catBreedInfo = listOf(catBreedInfo),
            favorite = true
        )

        assertEquals(catMainInfo1, catMainInfo2)
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

        val catMainInfo1 = CatMainInfo(
            id = "123",
            url = "https://example.com/cat.jpg",
            width = 800,
            height = 600,
            catBreedInfo = listOf(catBreedInfo1),
            favorite = true
        )

        val catMainInfo2 = CatMainInfo(
            id = "456",
            url = "https://example.com/cat2.jpg",
            width = 700,
            height = 500,
            catBreedInfo = listOf(catBreedInfo2),
            favorite = false
        )

        assertNotEquals(catMainInfo1, catMainInfo2)
    }
}