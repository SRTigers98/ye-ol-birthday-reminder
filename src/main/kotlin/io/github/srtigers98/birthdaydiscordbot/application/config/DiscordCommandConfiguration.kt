package io.github.srtigers98.birthdaydiscordbot.application.config

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
import kotlinx.coroutines.launch
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
class DiscordCommandConfiguration {

  private val log: Logger = LoggerFactory.getLogger(DiscordCommandConfiguration::class.java)

  /**
   * Deletes unused commands and registers all available bot commands as application commands.
   */
  @Bean(name = ["discord-commands"])
  fun discordCommands(kord: Kord, commands: List<BirthdayCommand>): List<GlobalChatInputCommand> = runBlocking {
    // Remove unused commands
    launch {
      kord.rest.interaction.getGlobalApplicationCommands(kord.selfId).forEach {
        if (commands.find { c -> c.name == it.name } == null) {
          kord.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
          log.info("Deleted command ${it.name}!")
        }
      }
    }

    // Register bot commands
    log.info("Registering ${commands.count()} command(s)!")
    commands.map {
      kord.createGlobalChatInputCommand(it.name, it.description, it.builder)
    }
  }
}
