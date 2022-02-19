package io.github.srtigers98.birthdaydiscordbot.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthdayDiscordBotApplication

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
