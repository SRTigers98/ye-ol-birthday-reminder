package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.service.MessageService
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BirthdayDiscordBotApplication(
  @Value("\${bot.token}") private val token: String,
  private val messageService: MessageService
) : CommandLineRunner {

  private val log: Logger = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java)

  override fun run(vararg args: String?) = runBlocking {
    val client = Kord(token)

    client.on<MessageCreateEvent> {
      if (message.content.startsWith("!") && message.author?.isBot == false) {
        messageService.handleMessage(message)
      }
    }

    log.info("Discord Bot started!")
    client.login()
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
