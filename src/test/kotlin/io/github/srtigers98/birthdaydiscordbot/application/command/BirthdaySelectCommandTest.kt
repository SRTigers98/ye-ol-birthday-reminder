package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.cache.data.InteractionData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import io.github.srtigers98.birthdaydiscordbot.application.service.GuildConfigService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class BirthdaySelectCommandTest {

  @InjectMocks
  private lateinit var tested: BirthdaySelectCommand

  @Mock
  private lateinit var guildConfigService: GuildConfigService

  @Test
  fun handleCommandTest() = runBlocking<Unit> {
    val interaction: ChatInputCommandInteraction = mock()

    val interactionUser: User = mock()
    val interactionData: InteractionData = mock()

    val guildId = "111"
    val channelId = "555"
    val member: Member = mock()
    val memberPermissions: Permissions = mock()

    whenever(interaction.data)
      .thenReturn(interactionData)
    whenever(interactionData.guildId)
      .thenReturn(OptionalSnowflake.Value(Snowflake(guildId)))
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.asMember(Snowflake(guildId)))
      .thenReturn(member)
    whenever(member.getPermissions())
      .thenReturn(memberPermissions)
    whenever(memberPermissions.contains(Permission.ManageGuild))
      .thenReturn(true)

    whenever(guildConfigService.selectGuildChannel(guildId, channelId))
      .thenReturn(mock())

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))

    verify(guildConfigService, times(1))
      .selectGuildChannel(guildId, channelId)
  }

  @Test
  fun handleCommandNoPermissionTest() = runBlocking<Unit> {
    val interaction: ChatInputCommandInteraction = mock()

    val interactionUser: User = mock()
    val interactionData: InteractionData = mock()

    val guildId = "111"
    val channelId = "555"
    val member: Member = mock()
    val memberPermissions: Permissions = mock()

    whenever(interaction.data)
      .thenReturn(interactionData)
    whenever(interactionData.guildId)
      .thenReturn(OptionalSnowflake.Value(Snowflake(guildId)))
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.asMember(Snowflake(guildId)))
      .thenReturn(member)
    whenever(member.getPermissions())
      .thenReturn(memberPermissions)
    whenever(memberPermissions.contains(Permission.ManageGuild))
      .thenReturn(false)

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))

    verify(guildConfigService, times(0))
      .selectGuildChannel(guildId, channelId)
  }
}
