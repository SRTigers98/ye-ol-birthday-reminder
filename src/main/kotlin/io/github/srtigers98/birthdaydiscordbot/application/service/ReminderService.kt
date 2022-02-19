package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReminderService(
  @Value("\${bot.token}") private val token: String,
  private val birthdayService: BirthdayService
) {

  private val client: RestClient = RestClient(token)

  @Scheduled(cron = "0 0 12 * * *")
  fun checkForBirthday() {
    val birthdays = birthdayService.checkForBirthdayOn(LocalDate.now())

    birthdays.forEach {
      runBlocking {
        val channelId = Snowflake(it.channelId)
        val msg = client.channel.createMessage(channelId) {
          content = "Happy Birthday ${it.mention}"
        }
        client.channel.createReaction(channelId, msg.id, "\uD83E\uDD73")
      }
    }
  }
}
