package com.crys.codingtask.other

import com.crys.codingtask.util.Converter
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConverterTest {

    //testing if stringToDate() is returning valid results
    private val converter =  Converter
    private val months = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    @Test
    fun `valid date returns valid result`() {
        val result = converter.stringToDate("2021-11-21", months)
        assertThat(result).isEqualTo("21 November 2021")
    }

    @Test
    fun `wrong string length returns invalid result`() {
        val date = "1111-11-111"
        val result = converter.stringToDate(date, months)
        assertThat(result).isEqualTo(date)
    }


    @Test
    fun `wrong month returns invalid result`() {
        val date = "2021-13-21"
        val result = converter.stringToDate(date, months)
        assertThat(result).isEqualTo(date)
    }

    @Test
    fun `wrong date format returns invalid result`() {
        val date = "21-11-2021"
        val result = converter.stringToDate(date, months)
        assertThat(result).isEqualTo(date)
    }

    //testing if is rounding
    @Test
    fun `round double`() {
        val number = 2391.0230230
        val result = converter.roundCurrency(number)
        assertThat(result).isEqualTo(2391.02302)
    }

}