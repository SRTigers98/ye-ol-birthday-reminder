package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthdayDiscordBotApplication(
  @Value("\${bot.token}") private val token: String,
  private val birthdayService: BirthdayService
) : CommandLineRunner {

  private val log: Logger = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java)

  override fun run(vararg args: String?) = runBlocking {
    val client = Kord(token)

    client.on<MessageCreateEvent> {
      if (!message.content.startsWith("!") || message.author?.isBot == true) {
        return@on
      }

      if (Regex("!bdayIs \\d{4}-\\d{2}-\\d{2}").matches(message.content)) {
        birthdayService.save(message, message.content.split(" ")[1])
        log.info("Birthday for user ${message.author?.username} saved successfully!")
        message.channel.createMessage("Hey ${message.author?.mention}, your birthday was saved successfully!")
      }
    }

    log.info("Discord Bot started!")
    client.login()
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
