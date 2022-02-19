package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.core.entity.Message
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

  fun save(msg: Message, birthdayInput: String) {
    val userId = msg.author?.id.toString()
    val mention = msg.author?.mention as String
    val channelId = msg.channelId.toString()

    val birthdayDate = LocalDate.parse(birthdayInput, formatter)
    if (birthdayDate.isAfter(LocalDate.now())) {
      throw BirthdayInFutureException(
        """The entered birthday *$birthdayDate* is in the future!
        |Your birthday was **not** saved, please try again!""".trimMargin()
      )
    }

    val birthday = Birthday(
      userId,
      channelId,
      mention,
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
