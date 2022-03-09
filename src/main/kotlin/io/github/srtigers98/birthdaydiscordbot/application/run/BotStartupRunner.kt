package io.github.srtigers98.birthdaydiscordbot.application.run

import dev.kord.core.Kord
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
) : ApplicationRunner {

  private val log: Logger = LoggerFactory.getLogger(BotStartupRunner::class.java)

  /**
   * Starts the discord bot.
   *
   * It clears all unused global commands, registers the global bot commands and sets up the event listener for the configured commands.
   * Finally, it logs into the bot and starts it.
   */
  override fun run(args: ApplicationArguments?) {
    runBlocking {
      // Remove unused commands
      kord.rest.interaction.getGlobalApplicationCommands(kord.selfId).forEach {
        if (commands.find { c -> c.name == it.name } == null) {
          kord.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
        }
      }

      // Register bot commands
      val discordCommands = commands.associate {
        it.name to kord.createGlobalChatInputCommand(it.name, it.description, it.builder)
      }.toMap()
      log.info("Successfully registered ${discordCommands.count()} command(s)!")

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
        if (command != null) {
          discordCommands[command.name]?.service?.createInteractionResponse(
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
