package io.github.srtigers98.birthdaydiscordbot.application.listener

import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayCommand
import kotlinx.coroutines.Job
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 * Listener for incoming slash commands.
 *
 * @author Benjamin Eder
 */
@Component
class CommandListener(
  private val commands: List<BirthdayCommand>,
  @Qualifier("discord-commands") private val discordCommands: List<GlobalChatInputCommand>,
) : KordListener<ChatInputCommandInteractionCreateEvent> {

  private val log: Logger = LoggerFactory.getLogger(CommandListener::class.java)

  override fun consumer(): suspend ChatInputCommandInteractionCreateEvent.() -> Unit = {
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

  override suspend fun register(kord: Kord): Job = kord.on(consumer = consumer())
}
