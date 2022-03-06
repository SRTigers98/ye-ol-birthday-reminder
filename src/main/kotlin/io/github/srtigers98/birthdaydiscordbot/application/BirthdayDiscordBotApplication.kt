package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main class for the bot application.
 *
 * @author Benjamin Eder
 */
@SpringBootApplication
@EnableScheduling
class BirthdayDiscordBotApplication(
  private val kord: Kord,
  private val commands: List<BirthdayCommand>
) : CommandLineRunner {

  private val log: Logger = LoggerFactory.getLogger(BirthdayDiscordBotApplication::class.java)

  /**
   * Starts the discord bot.
   *
   * It clears all unused global commands, registers the global bot commands and sets up the event listener for the configured commands.
   * Finally, it logs into the bot and starts it.
   */
  override fun run(vararg args: String?) = runBlocking {
    clearUnusedCommands()

    val discordCommands = commands.associate {
      it.name to kord.createGlobalChatInputCommand(it.name, it.description, it.builder)
    }.toMap()
    log.info("Successfully registered ${discordCommands.count()} command(s)!")

    kord.on<ChatInputCommandInteractionCreateEvent> {
      val command = commands.find { interaction.command.rootName == it.name }
      if (command != null) {
        discordCommands[command.name]?.service?.createInteractionResponse(
          interaction.id, interaction.token, command.handleCommand(interaction)
        )
      }
    }

    log.info("Discord Bot started!")
    kord.login()
  }

  private suspend fun clearUnusedCommands() {
    kord.rest.interaction.getGlobalApplicationCommands(kord.selfId).forEach {
      if (commands.find { c -> c.name == it.name } == null) {
        kord.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
      }
    }
  }
}

fun main(args: Array<String>) {
  runApplication<BirthdayDiscordBotApplication>(*args)
}
