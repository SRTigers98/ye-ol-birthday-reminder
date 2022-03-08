package io.github.srtigers98.birthdaydiscordbot.application.util

/**
 * Utility methods for birthday numbering.
 *
 * @author Benjamin Eder
 */
object BirthdayNumberUtil {

  /**
   * Returns the ordinal string representation for the given age.
   *
   * @param age the given age
   * @return an ordinal string representation
   */
  fun getOrdinalStringForAge(age: Int): String {
    // Exceptions in ordinal numbers
    if (listOf(11, 12, 13).contains(age)) {
      return "${age}th"
    }

    return when (age.toString().last()) {
      '1' -> "${age}st"
      '2' -> "${age}nd"
      '3' -> "${age}rd"
      else -> "${age}th"
    }
  }
}
