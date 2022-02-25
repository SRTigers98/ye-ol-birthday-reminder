package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.ChannelService
import dev.kord.rest.service.RestClient
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class ReminderServiceTest {

  @InjectMocks
  private lateinit var tested: ReminderService

  @Mock
  private lateinit var restClient: RestClient

  @Mock
  private lateinit var birthdayService: BirthdayService

  @Test
  fun checkForBirthdayTest() = runBlocking {
    val today = LocalDate.now()
    val foundBirthdays = listOf(
      Birthday("42", GuildConfig("1", "99"), "@42", today.year - 18, today.monthValue, today.dayOfMonth)
    )
    val channelService: ChannelService = mock()
    val discordMessage: DiscordMessage = mock()

    whenever(birthdayService.checkForBirthdayOn(today))
      .thenReturn(foundBirthdays)
    whenever(restClient.channel)
      .thenReturn(channelService)
    whenever(channelService.createMessage(Snowflake("99"), builder = {
      content = """
            |Happy Birthday ${foundBirthdays[0].mention}!
            |Congratulations to your ${today.year - foundBirthdays[0].birthdayYear}. birthday!
          """.trimMargin()
    }))
      .thenReturn(discordMessage)
    whenever(discordMessage.id)
      .thenReturn(Snowflake("1"))
    whenever(channelService.createReaction(eq(Snowflake("99")), eq(Snowflake("1")), any()))
      .thenAnswer { }

    tested.checkForBirthday()

    verify(channelService, times(1))
      .createMessage(Snowflake("99"), builder = {
        content = """
            |Happy Birthday ${foundBirthdays[0].mention}!
            |Congratulations to your ${today.year - foundBirthdays[0].birthdayYear}. birthday!
          """.trimMargin()
      })
    verify(channelService, times(1))
      .createReaction(eq(Snowflake("99")), eq(Snowflake("1")), any())
  }
}
