package com.catexplorer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkUtilsTest {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Mock
    private lateinit var mockNetworkInfo: NetworkInfo

    private lateinit var networkUtils: NetworkUtils

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkUtils = NetworkUtils(mockContext)
    }

    @Test
    fun testIsNetworkAvailable_WithNetworkConnection_ReturnsTrue() {
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(mockConnectivityManager)

        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(true)

        val result = networkUtils.isNetworkAvailable()

        assert(result)
    }

    @Test
    fun testIsNetworkAvailable_WithoutNetworkConnection_ReturnsFalse() {
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(mockConnectivityManager)

        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(null)

        val result = networkUtils.isNetworkAvailable()

        assert(!result)
    }

    @Test
    fun testIsNetworkAvailable_WithNetworkConnectionButNotConnected_ReturnsFalse() {
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(mockConnectivityManager)

        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(false)

        val result = networkUtils.isNetworkAvailable()

        assert(!result)
    }
}