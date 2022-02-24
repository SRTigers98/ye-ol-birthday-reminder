package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.Optional
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayNotFoundException
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BirthdayShowCommand(
  private val birthdayService: BirthdayService
) : BirthdayCommand("show", "Shows your currently stored birthday!", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val userId = interaction.user.id.toString()
    val userMention = interaction.user.mention
    val channelId = interaction.channelId.toString()

    val response = readBirthday(userId, userMention, channelId)

    return InteractionResponseCreateRequest(
      InteractionResponseType.ChannelMessageWithSource,
      Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke(response)))
    )
  }

  private fun readBirthday(userId: String, userMention: String, channelId: String): String {
    return try {
      val userBirthday = birthdayService.getUserBirthday(userId, channelId)
      val birthdayDate = LocalDate.of(userBirthday.birthdayYear, userBirthday.birthdayMonth, userBirthday.birthdayDay)
      "Saved birthday for $userMention is *$birthdayDate*!"
    } catch (e: BirthdayNotFoundException) {
      "No birthday saved for $userMention!"
    }
  }
}
