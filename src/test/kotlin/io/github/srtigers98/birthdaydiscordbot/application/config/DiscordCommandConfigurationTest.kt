package io.github.srtigers98.birthdaydiscordbot.application.config

import dev.kord.common.entity.DiscordApplicationCommand
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.InteractionService
import dev.kord.rest.service.RestClient
import io.github.srtigers98.birthdaydiscordbot.application.command.BirthdayVersionCommand
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

internal class DiscordCommandConfigurationTest {

  private val kord: Kord = mock {
    on { selfId } doReturn Snowflake("1")
  }
  private val versionCommand: BirthdayVersionCommand = mock {
    on { name } doReturn "version"
    on { description } doReturn "/version description"
    on { builder } doReturn { }
  }

  private val tested = DiscordCommandConfiguration(kord, listOf(versionCommand))

  @Test
  fun removeUnusedCommandsTest() = runBlocking {
    val interactionService: InteractionService = mock()
    val restClient: RestClient = mock {
      on { interaction } doReturn interactionService
    }

    val versionApplicationCommand: DiscordApplicationCommand = mock {
      on { id } doReturn Snowflake("42")
      on { applicationId } doReturn Snowflake("42")
      on { name } doReturn "version"
    }
    val unusedApplicationCommand: DiscordApplicationCommand = mock {
      on { id } doReturn Snowflake("99")
      on { applicationId } doReturn Snowflake("99")
      on { name } doReturn "unused"
    }

    whenever(kord.rest)
      .thenReturn(restClient)
    whenever(interactionService.getGlobalApplicationCommands(Snowflake("1")))
      .thenReturn(listOf(versionApplicationCommand, unusedApplicationCommand))
    whenever(interactionService.deleteGlobalApplicationCommand(any(), any()))
      .thenAnswer { }

    tested.removeUnusedCommands()

    verify(interactionService, times(1))
      .deleteGlobalApplicationCommand(Snowflake("99"), Snowflake("99"))
    verify(interactionService, times(0))
      .deleteGlobalApplicationCommand(Snowflake("42"), Snowflake("42"))
  }
}
