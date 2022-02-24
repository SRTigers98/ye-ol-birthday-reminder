package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayInFutureException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.format.DateTimeParseException

@ExtendWith(MockitoExtension::class)
internal class BirthdayServiceTest {

  @InjectMocks
  private lateinit var tested: BirthdayService

  @Mock
  private lateinit var birthdayRepository: BirthdayRepository

  @Test
  fun saveTest() {
    val birthday = Birthday("42", "1", "@42", 2000, 12, 12)

    whenever(birthdayRepository.save(birthday))
      .thenReturn(birthday)

    tested.save(birthday.userId, birthday.mention, birthday.channelId, "2000-12-12")

    verify(birthdayRepository, times(1)).save(birthday)
  }

  @Test
  fun saveInvalidDateFormatTest() {
    assertThrows<DateTimeParseException> {
      tested.save("1", "1", "1", "1-1-2000")
    }
  }

  @Test
  fun saveDateInFutureTest() {
    assertThrows<BirthdayInFutureException> {
      tested.save("1", "1", "1", "2999-12-31")
    }
  }

  @Test
  fun checkForBirthdayOnTest() {
    val foundBirthdays = listOf(
      Birthday("42", "1", "@42", 2000, 1, 1)
    )

    whenever(birthdayRepository.findByBirthdayMonthIsAndBirthdayDayIs(1, 1))
      .thenReturn(foundBirthdays)

    val result = tested.checkForBirthdayOn(LocalDate.of(2022, 1, 1))

    assertThat(result, `is`(foundBirthdays))
  }
}
