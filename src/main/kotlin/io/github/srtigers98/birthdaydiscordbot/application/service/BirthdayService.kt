package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.BirthdayId
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayExceptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Service that manages the stored birthdays.
 *
 * @author Benjamin Eder
 * @author Stephan Brunner <s.brunner@stephan-brunner.net>
 */
@Service
class BirthdayService(
  private val birthdayRepository: BirthdayRepository,
  private val guildConfigService: GuildConfigService
) {

  private val log: Logger = LoggerFactory.getLogger(BirthdayService::class.java)
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  /**
   * Creates a birthday from the given parameters.
   * The birthday will then be saved to the database.
   *
   * @param userId id of the user
   * @param userMention mention string for the user
   * @param guildId id of the current guild the user is in
   * @param currentChannelId the id of the channel the user used for interaction
   * @param birthdayInput input string for the birthday
   * @return The saved birthday
   * @throws BirthdayExceptions.BirthdayInFutureException if the given birthday is in the future
   */
  @Throws(BirthdayExceptions.BirthdayInFutureException::class)
  fun save(
    userId: String,
    userMention: String,
    guildId: String,
    currentChannelId: String,
    birthdayInput: String
  ): Birthday {
    val birthdayDate = LocalDate.parse(birthdayInput, formatter)
    if (birthdayDate.isAfter(LocalDate.now())) {
      throw BirthdayExceptions.BirthdayInFutureException
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

    log.info("Setting birthday for user {} in guild {} to {}", userId, guildId, formatter.format(birthdayDate))

    return birthdayRepository.save(birthday)
  }

  /**
   * Gets the stored birthday for the given user and the given guild.
   *
   * @param userId id of the user
   * @param guildId id of the current guild the user is in
   * @return The stored birthday
   * @throws BirthdayExceptions.BirthdayNotFoundException if there is no birthday stored
   */
  @Throws(BirthdayExceptions.BirthdayNotFoundException::class)
  fun getUserBirthday(userId: String, guildId: String): Birthday {
    val birthdayId = BirthdayId(userId, guildId)
    return birthdayRepository.findById(birthdayId)
      .orElseThrow { BirthdayExceptions.BirthdayNotFoundException }
  }

  /**
   * Gets all birthdays for the given date.
   * The dates are only checked for equal day and month.
   *
   * @param date the date to check
   * @return list with all birthdays on the given day
   */
  fun checkForBirthdayOn(date: LocalDate): List<Birthday> =
    birthdayRepository.findByBirthdayMonthIsAndBirthdayDayIs(
      date.monthValue,
      date.dayOfMonth
    )

  /**
   * Deletes the birthday for the given user and guild.
   *
   * @param userId id of the user
   * @param guildId id of the current guild the user is in
   */
  fun delete(userId: String, guildId: String) {
    log.info("Deleting birthday for user {} on guild {}", userId, guildId)

    val birthdayId = BirthdayId(userId, guildId)
    birthdayRepository.deleteById(birthdayId)
  }
}
