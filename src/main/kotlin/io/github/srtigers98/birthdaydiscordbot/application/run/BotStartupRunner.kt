package io.github.srtigers98.birthdaydiscordbot.application.run

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Application runner to start the discord bot.
 *
 * @author Benjamin Eder
 */
@Component
class BotStartupRunner(
  @Qualifier("discord-scope") private val scope: CoroutineScope,
  private val kord: Kord,
  private val commands: List<BirthdayCommand>,
  @Qualifier("discord-commands") private val discordCommands: List<GlobalChatInputCommand>,
) : ApplicationRunner {

  private val log: Logger = LoggerFactory.getLogger(BotStartupRunner::class.java)

  /**
   * Starts the discord bot.
   */
  override fun run(args: ApplicationArguments?) {
    runBlocking {
      // Setup command listener
      kord.on<ChatInputCommandInteractionCreateEvent> {
        log.info(
          "{}/{} issued command {} on guild {}",
          interaction.user.username,
          interaction.user.id,
          interaction.command.rootName,
          interaction.data.guildId.asOptional.value
        )

        val command = commands.find { interaction.command.rootName == it.name }
        val discordCommand = discordCommands.find { interaction.command.rootName == it.name }
        if (command != null && discordCommand != null) {
          discordCommand.service.createInteractionResponse(
            interaction.id, interaction.token, command.handleCommand(interaction)
          )
        }
      }
    }

    // Start the bot
    scope.launch { kord.login() }
    log.info("Discord Bot started!")
  }
}
