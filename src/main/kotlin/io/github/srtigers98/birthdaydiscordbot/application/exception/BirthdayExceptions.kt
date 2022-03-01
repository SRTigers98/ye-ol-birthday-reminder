package io.github.srtigers98.birthdaydiscordbot.application.exception

object BirthdayExceptions {

  object BirthdayInFutureException :
    Exception("The given birthday is in the future!")

  object BirthdayNotFoundException :
    Exception("No saved birthday found for the given user in the current guild!")
}
