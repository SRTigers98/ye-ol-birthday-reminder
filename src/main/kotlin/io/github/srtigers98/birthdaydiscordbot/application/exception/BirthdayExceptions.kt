package io.github.srtigers98.birthdaydiscordbot.application.exception

/**
 * Object which provides all birthday related exceptions.
 *
 * @author Benjamin Eder
 */
object BirthdayExceptions {

  object BirthdayInFutureException :
    Exception("The given birthday is in the future!")

  object BirthdayNotFoundException :
    Exception("No saved birthday found for the given user in the current guild!")
}
