package io.github.srtigers98.birthdaydiscordbot.application.dto

import java.io.Serializable

/**
 * The key for a birthday entry.
 *
 * @author Benjamin Eder
 */
data class BirthdayId(
  val userId: String = "",
  val guild: String = ""
) : Serializable
