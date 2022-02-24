package io.github.srtigers98.birthdaydiscordbot.application.dto

import java.io.Serializable

data class BirthdayId(
  val userId: String = "",
  val guild: String = ""
) : Serializable
