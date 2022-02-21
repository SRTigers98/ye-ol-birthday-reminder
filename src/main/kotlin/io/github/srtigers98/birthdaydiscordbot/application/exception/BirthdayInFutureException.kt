package io.github.srtigers98.birthdaydiscordbot.application.exception

class BirthdayInFutureException(
  message: String? = null,
  cause: Throwable? = null
) : BirthdayException(message, cause)
