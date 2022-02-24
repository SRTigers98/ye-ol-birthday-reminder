package io.github.srtigers98.birthdaydiscordbot.application.exception

class BirthdayNotFoundException(
  message: String? = null,
  cause: Throwable? = null
) : BirthdayException(message, cause)
