package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.GuildConfigRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*
import jakarta.persistence.EntityManager

@ExtendWith(MockitoExtension::class)
internal class GuildConfigServiceTest {

  @InjectMocks
  private lateinit var tested: GuildConfigService

  @Mock
  private lateinit var guildConfigRepository: GuildConfigRepository

  @Mock
  private lateinit var entityManager: EntityManager

  @Test
  fun selectGuildChannelTest() {
    val guildId = UUID.randomUUID().toString()
    val channelId = UUID.randomUUID().toString()

    val guildConfig = GuildConfig(guildId, channelId)

    whenever(guildConfigRepository.save(guildConfig))
      .thenReturn(guildConfig)

    val result = tested.selectGuildChannel(guildId, channelId)

    assertThat(result, `is`(guildConfig))

    verify(guildConfigRepository, times(1))
      .save(guildConfig)
  }

  @Test
  fun getGuildConfigTest() {
    val guildId = UUID.randomUUID().toString()
    val channelId = UUID.randomUUID().toString()
    val currentChannelId = UUID.randomUUID().toString()

    val guildConfig = GuildConfig(guildId, channelId)

    whenever(guildConfigRepository.findById(guildId))
      .thenReturn(Optional.of(guildConfig))

    val result = tested.getGuildConfig(guildId, currentChannelId)

    assertThat(result, `is`(guildConfig))
  }

  @Test
  fun getGuildConfigNotPresentTest() {
    val guildId = UUID.randomUUID().toString()
    val currentChannelId = UUID.randomUUID().toString()

    whenever(guildConfigRepository.findById(guildId))
      .thenReturn(Optional.empty())

    val result = tested.getGuildConfig(guildId, currentChannelId)

    assertThat(result, `is`(GuildConfig(guildId, currentChannelId)))
  }

  @Test
  fun getAllGuildConfigsTest() {
    val config1: GuildConfig = mock()
    val config2: GuildConfig = mock()

    whenever(guildConfigRepository.findAll())
      .thenReturn(listOf(config1, config2))

    val result = tested.getAllGuildConfigs()

    assertThat(result.size, `is`(2))
    assertThat(result, hasItems(config1, config2))
  }

  @Test
  fun deleteGuildConfigTest() {
    val config1: GuildConfig = mock()
    val config2: GuildConfig = mock()

    whenever(guildConfigRepository.deleteAll(any()))
      .thenAnswer { }
    whenever(entityManager.clear())
      .thenAnswer { }

    tested.deleteGuildConfig(config1, config2)

    verify(guildConfigRepository, times(1))
      .deleteAll(listOf(config1, config2))
    verify(entityManager, times(1))
      .clear()
  }
}
