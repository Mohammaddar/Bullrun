package com.example.bullrun.ui.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import drewcarlson.coingecko.models.coins.data.Links
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class CoinInfoUITest {

    lateinit var coinInfo: CoinInfoUI
    //Given
    @Before
    fun setUpCoinInfo() {
        coinInfo=CoinInfoUI(
            "id",
            "sym",
            "name",
            null,
            65L,
            10.000000,
            8.446421,
            2222174.44843,
            100000.0000,
            50000.0000,
            1.754684f,
            1.78678,
            "desc",
            null,
            15.000000,
            32.4661315,
            -52.541358,
            null,
            null,
            Links()
        )
    }

    @Test
    fun progress24H_nullInput_return0() {
        //When


        //Then
        assertThat(coinInfo.progress24H, `is` (0f))
    }
}