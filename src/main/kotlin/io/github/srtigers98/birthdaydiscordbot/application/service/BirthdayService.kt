package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayInFutureException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class BirthdayService(
  private val birthdayRepository: BirthdayRepository
) {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  @Throws(BirthdayInFutureException::class)
  fun save(userId: String, userMention: String, channelId: String, birthdayInput: String) {
    val birthdayDate = LocalDate.parse(birthdayInput, formatter)
    if (birthdayDate.isAfter(LocalDate.now())) {
      throw BirthdayInFutureException()
    }

    val birthday = Birthday(
      userId,
      channelId,
      userMention,
      birthdayDate.year,
      birthdayDate.monthValue,
      birthdayDate.dayOfMonth
    )

    birthdayRepository.save(birthday)
  }

  fun checkForBirthdayOn(date: LocalDate): List<Birthday> {
    return birthdayRepository.findByBirthdayMonthIsAndBirthdayDayIs(
      date.monthValue,
      date.dayOfMonth
    )
  }
}
