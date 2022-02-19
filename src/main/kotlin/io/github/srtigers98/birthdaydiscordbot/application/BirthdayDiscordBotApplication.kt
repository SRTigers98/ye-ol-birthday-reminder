package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthdayDiscordBotApplication(
  @Value("\${bot.token}") private val token: String
) : CommandLineRunner {

  private val log: Logger = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java)

  override fun run(vararg args: String?) = runBlocking {
    val client = Kord(token)

    client.on<MessageCreateEvent> {
      log.info("Message received: ${message.content}")
    }

    log.info("Discord Bot started!")
    client.login()
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
