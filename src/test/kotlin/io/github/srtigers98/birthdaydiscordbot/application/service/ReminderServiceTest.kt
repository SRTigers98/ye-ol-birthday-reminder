package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.*
import dev.kord.rest.json.request.StartThreadRequest
import dev.kord.rest.service.ChannelService
import dev.kord.rest.service.RestClient
import dev.kord.rest.service.UserService
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import io.github.srtigers98.birthdaydiscordbot.application.util.BirthdayNumberUtil
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
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
    val userService: UserService = mock()
    val discordUser: DiscordUser = mock()
    val channelService: ChannelService = mock()
    val threadChannel: DiscordChannel = mock()
    val discordMessage: DiscordMessage = mock()

    whenever(birthdayService.checkForBirthdayOn(today))
      .thenReturn(foundBirthdays)
    whenever(restClient.user)
      .thenReturn(userService)
    whenever(userService.getUser(Snowflake("42")))
      .thenReturn(discordUser)
    whenever(discordUser.username)
      .thenReturn("arthur")
    whenever(restClient.channel)
      .thenReturn(channelService)
    whenever(channelService.startThread(eq(Snowflake("99")), anyOrNull<StartThreadRequest>(), anyOrNull()))
      .thenReturn(threadChannel)
    whenever(threadChannel.id)
      .thenReturn(Snowflake("123"))
    whenever(channelService.createMessage(Snowflake("123"), builder = {
      content = """
            |Happy Birthday ${foundBirthdays[0].mention}!
            |Congratulations to your ${BirthdayNumberUtil.getOrdinalStringForAge(today.year - foundBirthdays[0].birthdayYear)} birthday!
          """.trimMargin()
    }))
      .thenReturn(discordMessage)
    whenever(discordMessage.id)
      .thenReturn(Snowflake("1"))
    whenever(channelService.createReaction(eq(Snowflake("123")), eq(Snowflake("1")), any()))
      .thenAnswer { }

    tested.checkForBirthday()

    val startThreadCapture = argumentCaptor<StartThreadRequest>()

    verify(channelService, times(1))
      .startThread(eq(Snowflake("99")), startThreadCapture.capture(), anyOrNull())
    verify(channelService, times(1))
      .createMessage(Snowflake("123"), builder = {
        content = """
            |Happy Birthday ${foundBirthdays[0].mention}!
            |Congratulations to your ${BirthdayNumberUtil.getOrdinalStringForAge(today.year - foundBirthdays[0].birthdayYear)} birthday!
          """.trimMargin()
      })
    verify(channelService, times(1))
      .createReaction(eq(Snowflake("123")), eq(Snowflake("1")), any())

    val startThreadRequest = startThreadCapture.firstValue
    assertThat(startThreadRequest, `is`(notNullValue()))
    assertThat(startThreadRequest.name, `is`("birthday-arthur-${today.year}"))
    assertThat(startThreadRequest.autoArchiveDuration.value, `is`(ArchiveDuration.Day))
    assertThat(startThreadRequest.type.value, `is`(ChannelType.PublicGuildThread))
  }
}
