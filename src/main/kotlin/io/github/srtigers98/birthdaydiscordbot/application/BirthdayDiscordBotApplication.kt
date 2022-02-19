package io.github.srtigers98.birthdaydiscordbot.application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthdayDiscordBotApplication : CommandLineRunner {

  val log = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java) as Logger

  override fun run(vararg args: String?) {
    log.info("Hello World!")
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
