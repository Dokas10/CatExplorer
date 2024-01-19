package com.catexplorer.utils

import com.catexplorer.data.CatBreedInfo
import com.catexplorer.data.CatMainInfo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call

class ApiServiceTest {
    @Mock
    private lateinit var mockApiService: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetCatBreedList() {
        val mockCall = mock(Call::class.java) as Call<ArrayList<CatMainInfo>>
        `when`(mockApiService.getCatBreedList(10, 1)).thenReturn(mockCall)
        val result = mockApiService.getCatBreedList(10, 1)
        verify(mockApiService).getCatBreedList(10, 1)
    }

    @Test
    fun testGetCatBreedInfo() {
        val mockCall = mock(Call::class.java) as Call<CatMainInfo>
        `when`(mockApiService.getCatBreedInfo("beng")).thenReturn(mockCall)
        val result = mockApiService.getCatBreedInfo("beng")
        verify(mockApiService).getCatBreedInfo("beng")
    }

    @Test
    fun testGetAllCatBreeds() {
        val mockCall = mock(Call::class.java) as Call<ArrayList<CatBreedInfo>>
        `when`(mockApiService.getAllCatBreeds()).thenReturn(mockCall)
        val result = mockApiService.getAllCatBreeds()
        verify(mockApiService).getAllCatBreeds()
    }

    @Test
    fun testGetCatImagesByBreed() {
        val mockCall = mock(Call::class.java) as Call<ArrayList<CatMainInfo>>
        `when`(mockApiService.getCatImagesByBreed(5, "persian")).thenReturn(mockCall)
        val result = mockApiService.getCatImagesByBreed(5, "persian")
        verify(mockApiService).getCatImagesByBreed(5, "persian")
    }
}