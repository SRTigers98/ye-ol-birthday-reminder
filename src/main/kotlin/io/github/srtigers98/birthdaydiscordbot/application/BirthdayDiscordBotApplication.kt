package io.github.srtigers98.birthdaydiscordbot.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main class for the bot application.
 *
 * @author Benjamin Eder
 * @author Stephan Brunner <s.brunner@stephan-brunner.net>
 */
@SpringBootApplication
@EnableScheduling
class BirthdayDiscordBotApplication

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
