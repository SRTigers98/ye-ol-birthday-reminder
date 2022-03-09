package io.github.srtigers98.birthdaydiscordbot.application.listener

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.cache.data.InteractionData
import dev.kord.core.entity.User
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.InteractionService
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayVersionCommand
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

internal class CommandListenerTest {

  private val versionCommand: BirthdayVersionCommand = mock {
    on { name } doReturn "version"
  }
  private val discordVersionCommand: GlobalChatInputCommand = mock {
    on { name } doReturn "version"
  }

  private val tested = CommandListener(listOf(versionCommand), listOf(discordVersionCommand))

  @Test
  fun consumerTest() = runBlocking {
    val interactionResponse: InteractionResponseCreateRequest = mock()
    val interactionService: InteractionService = mock()
    val interactionUser: User = mock {
      on { id } doReturn Snowflake("42")
      on { username } doReturn "ArthurDent"
    }
    val interactionCommand: InteractionCommand = mock {
      on { rootName } doReturn "version"
    }
    val interactionData: InteractionData = mock {
      on { guildId } doReturn OptionalSnowflake.Value(Snowflake("1"))
    }
    val inputInteraction: ChatInputCommandInteraction = mock {
      on { id } doReturn Snowflake("99")
      on { token } doReturn "my-awesome-token"
      on { user } doReturn interactionUser
      on { command } doReturn interactionCommand
      on { data } doReturn interactionData
    }
    val event: ChatInputCommandInteractionCreateEvent = mock {
      on { interaction } doReturn inputInteraction
    }

    whenever(discordVersionCommand.service)
      .thenReturn(interactionService)
    whenever(versionCommand.handleCommand(inputInteraction))
      .thenReturn(interactionResponse)
    whenever(interactionService.createInteractionResponse(Snowflake("99"), "my-awesome-token", interactionResponse))
      .thenAnswer { }

    tested.consumer().invoke(event)

    verify(interactionService, times(1))
      .createInteractionResponse(Snowflake("99"), "my-awesome-token", interactionResponse)
  }

  @Test
  fun consumerUnknownCommandTest() = runBlocking {
    val interactionUser: User = mock {
      on { id } doReturn Snowflake("42")
      on { username } doReturn "ArthurDent"
    }
    val interactionCommand: InteractionCommand = mock {
      on { rootName } doReturn "unknown"
    }
    val interactionData: InteractionData = mock {
      on { guildId } doReturn OptionalSnowflake.Value(Snowflake("1"))
    }
    val inputInteraction: ChatInputCommandInteraction = mock {
      on { id } doReturn Snowflake("99")
      on { token } doReturn "my-awesome-token"
      on { user } doReturn interactionUser
      on { command } doReturn interactionCommand
      on { data } doReturn interactionData
    }
    val event: ChatInputCommandInteractionCreateEvent = mock {
      on { interaction } doReturn inputInteraction
    }

    tested.consumer().invoke(event)
  }
}
