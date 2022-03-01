package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.cache.data.InteractionData
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayExceptions
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class BirthdayShowCommandTest {

  @InjectMocks
  private lateinit var tested: BirthdayShowCommand

  @Mock
  private lateinit var birthdayService: BirthdayService

  @Test
  fun handleCommandTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val interactionUser: User = mock()
    val interactionData: InteractionData = mock()

    val userId = "111"
    val userMention = "<@$userId>"
    val guildId = "2"
    val birthday = Birthday(userId, GuildConfig(guildId, "99"), userMention, 2000, 1, 1)

    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.data)
      .thenReturn(interactionData)
    whenever(interactionData.guildId)
      .thenReturn(OptionalSnowflake.Value(Snowflake(guildId)))

    whenever(birthdayService.getUserBirthday(userId, guildId))
      .thenReturn(birthday)

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))
  }

  @Test
  fun handleCommandBirthdayNotFoundTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val interactionUser: User = mock()
    val interactionData: InteractionData = mock()

    val userId = "111"
    val userMention = "<@$userId>"
    val guildId = "2"

    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.data)
      .thenReturn(interactionData)
    whenever(interactionData.guildId)
      .thenReturn(OptionalSnowflake.Value(Snowflake(guildId)))

    whenever(birthdayService.getUserBirthday(userId, guildId))
      .thenThrow(BirthdayExceptions.BirthdayNotFoundExceptions)

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))
  }
}
