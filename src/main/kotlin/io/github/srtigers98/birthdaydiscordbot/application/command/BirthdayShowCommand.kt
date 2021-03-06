package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayExceptions
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Command to show the stored birthday of the user on its current guild.
 *
 * @author Benjamin Eder
 */
@Component
class BirthdayShowCommand(
  private val birthdayService: BirthdayService
) : BirthdayCommand("show", "Shows your currently stored birthday.", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val userId = interaction.user.id.toString()
    val userMention = interaction.user.mention
    val guildId = interaction.data.guildId.value.toString()

    val response = readBirthday(userId, userMention, guildId)

    return this.createResponse(InteractionResponseType.ChannelMessageWithSource, response)
  }

  private fun readBirthday(userId: String, userMention: String, guildId: String): String =
    try {
      val userBirthday = birthdayService.getUserBirthday(userId, guildId)
      val birthdayDate = LocalDate.of(userBirthday.birthdayYear, userBirthday.birthdayMonth, userBirthday.birthdayDay)
      "Saved birthday for $userMention is *$birthdayDate*!"
    } catch (e: BirthdayExceptions.BirthdayNotFoundException) {
      "No birthday saved for $userMention!"
    }
}
