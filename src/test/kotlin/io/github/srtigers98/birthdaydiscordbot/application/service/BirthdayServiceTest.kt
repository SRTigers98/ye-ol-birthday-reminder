package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.BirthdayId
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayExceptions
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class BirthdayServiceTest {

  @InjectMocks
  private lateinit var tested: BirthdayService

  @Mock
  private lateinit var birthdayRepository: BirthdayRepository

  @Mock
  private lateinit var guildConfigService: GuildConfigService

  @Test
  fun saveTest() {
    val guild = GuildConfig("1", "99")
    val birthday = Birthday("42", guild, "@42", 2000, 12, 12)

    whenever(guildConfigService.getGuildConfig(guild.guildId, guild.birthdayChannelId))
      .thenReturn(guild)
    whenever(birthdayRepository.save(birthday))
      .thenReturn(birthday)

    val result = tested.save(
      birthday.userId,
      birthday.mention,
      birthday.guild.guildId,
      birthday.guild.birthdayChannelId,
      "2000-12-12"
    )

    assertThat(result, `is`(birthday))

    verify(birthdayRepository, times(1)).save(birthday)
  }

  @Test
  fun saveInvalidDateFormatTest() {
    assertThrows<DateTimeParseException> {
      tested.save("1", "1", "1", "99", "1-1-2000")
    }
  }

  @Test
  fun saveDateInFutureTest() {
    assertThrows<BirthdayExceptions.BirthdayInFutureException> {
      tested.save("1", "1", "1", "99", "2999-12-31")
    }
  }

  @Test
  fun getUserBirthdayTest() {
    val userId = "42"
    val guildId = "1"

    val birthdayId = BirthdayId(userId, guildId)
    val birthday: Birthday = mock()

    whenever(birthdayRepository.findById(birthdayId))
      .thenReturn(Optional.of(birthday))

    val result = tested.getUserBirthday(userId, guildId)

    assertThat(result, `is`(birthday))
  }

  @Test
  fun getUserBirthdayNotFoundTest() {
    val userId = "99"
    val guildId = "55"

    val birthdayId = BirthdayId(userId, guildId)

    whenever(birthdayRepository.findById(birthdayId))
      .thenReturn(Optional.empty())

    assertThrows<BirthdayExceptions.BirthdayNotFoundException> {
      tested.getUserBirthday(userId, guildId)
    }
  }

  @Test
  fun checkForBirthdayOnTest() {
    val foundBirthdays = listOf(
      Birthday("42", GuildConfig("1", "99"), "@42", 2000, 1, 1)
    )

    whenever(birthdayRepository.findByBirthdayMonthIsAndBirthdayDayIs(1, 1))
      .thenReturn(foundBirthdays)

    val result = tested.checkForBirthdayOn(LocalDate.of(2022, 1, 1))

    assertThat(result, `is`(foundBirthdays))
  }

  @Test
  fun deleteTest() {
    val userId = "42"
    val guildId = "1"

    val birthdayId = BirthdayId(userId, guildId)

    whenever(birthdayRepository.deleteById(birthdayId))
      .thenAnswer { }

    tested.delete(userId, guildId)

    verify(birthdayRepository, times(1))
      .deleteById(birthdayId)
  }
}
