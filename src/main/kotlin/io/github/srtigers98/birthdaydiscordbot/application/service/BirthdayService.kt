package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.BirthdayId
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayExceptions
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class BirthdayService(
  private val birthdayRepository: BirthdayRepository,
  private val guildConfigService: GuildConfigService
) {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  @Throws(BirthdayExceptions.BirthdayInFutureExceptions::class)
  fun save(
    userId: String,
    userMention: String,
    guildId: String,
    currentChannelId: String,
    birthdayInput: String
  ): Birthday {
    val birthdayDate = LocalDate.parse(birthdayInput, formatter)
    if (birthdayDate.isAfter(LocalDate.now())) {
      throw BirthdayExceptions.BirthdayInFutureExceptions
    }

    val guildConfig = guildConfigService.getGuildConfig(guildId, currentChannelId)

    val birthday = Birthday(
      userId,
      guildConfig,
      userMention,
      birthdayDate.year,
      birthdayDate.monthValue,
      birthdayDate.dayOfMonth
    )

    return birthdayRepository.save(birthday)
  }

  @Throws(BirthdayExceptions.BirthdayNotFoundExceptions::class)
  fun getUserBirthday(userId: String, guildId: String): Birthday {
    val birthdayId = BirthdayId(userId, guildId)
    return birthdayRepository.findById(birthdayId)
      .orElseThrow { BirthdayExceptions.BirthdayNotFoundExceptions }
  }

  fun checkForBirthdayOn(date: LocalDate): List<Birthday> =
    birthdayRepository.findByBirthdayMonthIsAndBirthdayDayIs(
      date.monthValue,
      date.dayOfMonth
    )

  fun delete(userId: String, guildId: String) {
    val birthdayId = BirthdayId(userId, guildId)
    birthdayRepository.deleteById(birthdayId)
  }
}
