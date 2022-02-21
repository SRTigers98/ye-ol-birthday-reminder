package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
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
  private val commands: List<BirthdayCommand>
) : CommandLineRunner {

  private val log: Logger = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java)

  override fun run(vararg args: String?) = runBlocking {
    val client = Kord(token)

    // Clear all previous commands
    // client.rest.interaction.getGlobalApplicationCommands(client.selfId).forEach {
    //   client.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
    // }

    val discordCommands = commands.associate {
      it.name to client.createGlobalChatInputCommand(it.name, it.description, it.builder())
    }.toMap()
    log.info("Successfully registered ${discordCommands.count()} command(s)!")

    client.on<ChatInputCommandInteractionCreateEvent> {
      val command = commands.find { it.checkCommand(interaction) }
      if (command != null) {
        discordCommands[command.name]?.service?.createInteractionResponse(
          interaction.id, interaction.token, command.handleCommand(interaction)
        )
      }
    }

    log.info("Discord Bot started!")
    client.login()
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
