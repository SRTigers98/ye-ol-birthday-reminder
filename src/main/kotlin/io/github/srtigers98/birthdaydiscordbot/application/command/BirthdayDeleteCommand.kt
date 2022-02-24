package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.Optional
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.springframework.stereotype.Component

@Component
class BirthdayDeleteCommand(
  private val birthdayService: BirthdayService
) : BirthdayCommand("delete", "Deletes your saved birthday.", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val userId = interaction.user.id.toString()
    val userMention = interaction.user.mention
    val channelId = interaction.data.guildId.value.toString()

    birthdayService.delete(userId, channelId)
    val response = "Birthday for $userMention was deleted!"

    return InteractionResponseCreateRequest(
      InteractionResponseType.ChannelMessageWithSource,
      Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke(response)))
    )
  }
}
