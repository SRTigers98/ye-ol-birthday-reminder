package io.github.srtigers98.birthdaydiscordbot.application.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class BirthdayNumberUtilTest {

  @Test
  fun getBirthdayNumberForAge1Test() {
    testBirthdayNumberForAge(1, "1st")
  }

  @Test
  fun getBirthdayNumberForAge2Test() {
    testBirthdayNumberForAge(2, "2nd")
  }

  @Test
  fun getBirthdayNumberForAge3Test() {
    testBirthdayNumberForAge(3, "3rd")
  }

  @Test
  fun getBirthdayNumberForAge4Test() {
    testBirthdayNumberForAge(4, "4th")
  }

  @Test
  fun getBirthdayNumberForAge11Test() {
    testBirthdayNumberForAge(11, "11th")
  }

  @Test
  fun getBirthdayNumberForAge12Test() {
    testBirthdayNumberForAge(12, "12th")
  }

  @Test
  fun getBirthdayNumberForAge13Test() {
    testBirthdayNumberForAge(13, "13th")
  }

  @Test
  fun getBirthdayNumberForAge14Test() {
    testBirthdayNumberForAge(14, "14th")
  }

  @Test
  fun getBirthdayNumberForAge21Test() {
    testBirthdayNumberForAge(21, "21st")
  }

  @Test
  fun getBirthdayNumberForAge22Test() {
    testBirthdayNumberForAge(22, "22nd")
  }

  @Test
  fun getBirthdayNumberForAge23Test() {
    testBirthdayNumberForAge(23, "23rd")
  }

  @Test
  fun getBirthdayNumberForAge24Test() {
    testBirthdayNumberForAge(24, "24th")
  }

  private fun testBirthdayNumberForAge(age: Int, expected: String) {
    val result = BirthdayNumberUtil.getOrdinalStringForAge(age)

    assertThat(result, `is`(expected))
  }
}
