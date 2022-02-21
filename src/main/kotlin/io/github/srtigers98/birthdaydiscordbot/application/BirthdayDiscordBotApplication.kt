package io.github.srtigers98.birthdaydiscordbot.application

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.map
import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
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

    // Clear all previous commands
    client.rest.interaction.getGlobalApplicationCommands(client.selfId).forEach {
      client.rest.interaction.deleteGlobalApplicationCommand(it.applicationId, it.id)
    }

    val bdayIsCommand = client.createGlobalChatInputCommand("bdayis", "Saves your birthday to the server.") {
      options = mutableListOf(
        StringChoiceBuilder("birthday", "Your birthday.").apply {
          required = true
        }
      )
    }

    client.on<ChatInputCommandInteractionCreateEvent> {
      if (interaction.data.data.name.map { it == bdayIsCommand.name }.value == true) {
        log.info(interaction.data.data.options.toString())
        bdayIsCommand.service.createInteractionResponse(
          interaction.id, interaction.token,
          InteractionResponseCreateRequest(
            InteractionResponseType.ChannelMessageWithSource,
            Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke("Reacted to the command!")))
          )
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
