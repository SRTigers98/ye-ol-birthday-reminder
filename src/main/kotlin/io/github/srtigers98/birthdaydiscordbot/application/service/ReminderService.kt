package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.ArchiveDuration
import dev.kord.common.entity.Snowflake
import dev.kord.rest.json.request.StartThreadRequest
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Service to read the saved birthdays every day and congratulate the guild member (reminds the other guild members).
 *
 * @author Benjamin Eder
 * @author Stephan Brunner <s.brunner@stephan-brunner.net>
 */
@Service
class ReminderService(
  private val restClient: RestClient,
  private val birthdayService: BirthdayService
) {

  private val log: Logger = LoggerFactory.getLogger(ReminderService::class.java)
  private val logDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  /**
   * Check every day at 12 pm if any saved birthday is today.
   * If there are birthdays today, send a congratulation message to the guild member.
   * The message will be sent to a new thread in the configured birthday channel in the guild.
   */
  @Scheduled(cron = "0 0 12 * * *")
  fun checkForBirthday() {
    val today = LocalDate.now()

    log.info("Checking for birthdays on {}", logDateFormatter.format(today))

    val birthdays = birthdayService.checkForBirthdayOn(today)

    birthdays.forEach {
      runBlocking {
        val channelId = Snowflake(it.guild.birthdayChannelId)
        val userName = restClient.user.getUser(Snowflake(it.userId)).username

        val thread = restClient.channel.startThread(
          channelId, StartThreadRequest(
            name = "birthday-$userName-${today.year}",
            autoArchiveDuration = ArchiveDuration.Day,
          )
        )

        val msg = restClient.channel.createMessage(thread.id) {
          content = """
            |Happy Birthday ${it.mention}!
            |Congratulations to your ${today.year - it.birthdayYear}. birthday!
          """.trimMargin()
        }
        restClient.channel.createReaction(thread.id, msg.id, "\uD83E\uDD73")
      }
    }
  }
}
