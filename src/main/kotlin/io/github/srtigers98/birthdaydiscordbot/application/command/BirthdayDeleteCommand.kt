package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.springframework.stereotype.Component

/**
 * Command to delete the currently stored birthday of the user in its current guild.
 *
 * @author Benjamin Eder
 */
@Component
class BirthdayDeleteCommand(
  private val birthdayService: BirthdayService
) : BirthdayCommand("delete", "Deletes your saved birthday.", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val userId = interaction.user.id.toString()
    val userMention = interaction.user.mention
    val guildId = interaction.data.guildId.value.toString()

    birthdayService.delete(userId, guildId)
    val response = "Birthday for $userMention was deleted!"

    return this.createResponse(InteractionResponseType.ChannelMessageWithSource, response)
  }
}
