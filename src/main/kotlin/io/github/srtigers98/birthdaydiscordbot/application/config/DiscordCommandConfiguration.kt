package io.github.srtigers98.birthdaydiscordbot.application.config

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration to configure the bot commands.
 *
 * @author Benjamin Eder
 */
@Configuration
class DiscordCommandConfiguration(
  private val kord: Kord,
  private val commands: List<BirthdayCommand>,
) {

  private val log: Logger = LoggerFactory.getLogger(DiscordCommandConfiguration::class.java)

  init {
    removeUnusedCommands()
  }

  /**
   * Registers all available bot commands as application commands.
   */
  @Bean(name = ["discord-commands"])
  fun discordCommands(): List<GlobalChatInputCommand> = runBlocking {
    log.info("Registering ${commands.count()} command(s)!")
    commands.map {
      kord.createGlobalChatInputCommand(it.name, it.description, it.builder)
    }
  }

  /**
   * Deletes all unused application commands.
   */
  final fun removeUnusedCommands() = runBlocking {
    kord.rest.interaction.getGlobalApplicationCommands(kord.selfId).forEach {
      if (commands.find { c -> c.name == it.name } == null) {
        kord.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
        log.info("Deleted command ${it.name}!")
      }
    }
  }
}
