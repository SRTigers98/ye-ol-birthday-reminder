package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.DiscordPartialGuild
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import dev.kord.rest.service.UserService
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
internal class CleanupServiceTest {

  @InjectMocks
  private lateinit var tested: CleanupService

  @Mock
  private lateinit var restClient: RestClient

  @Mock
  private lateinit var guildConfigService: GuildConfigService

  @Test
  fun cleanupDatabaseTest() = runBlocking {
    val guildConfig: GuildConfig = mock {
      on { guildId } doReturn "1"
    }
    val staleConfig: GuildConfig = mock {
      on { guildId } doReturn "99"
    }
    val enrolledGuild: DiscordPartialGuild = mock {
      on { id } doReturn Snowflake("1")
    }
    val userService: UserService = mock()

    whenever(guildConfigService.getAllGuildConfigs())
      .thenReturn(listOf(guildConfig, staleConfig))
    whenever(restClient.user)
      .thenReturn(userService)
    whenever(userService.getCurrentUserGuilds())
      .thenReturn(listOf(enrolledGuild))
    whenever(guildConfigService.deleteGuildConfig(anyVararg()))
      .thenAnswer { }

    tested.cleanupDatabase()

    verify(guildConfigService, times(1))
      .deleteGuildConfig(staleConfig)
  }
}
