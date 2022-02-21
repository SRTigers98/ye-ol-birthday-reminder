package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.Optional
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import org.springframework.stereotype.Component

@Component
class BirthdaySaveCommand : BirthdayCommand("bdayis", "Saves your birthday to the server.") {

  override fun builder(): ChatInputCreateBuilder.() -> Unit = {
    options = mutableListOf(
      StringChoiceBuilder("birthday", "Your birthday.").apply {
        required = true
      }
    )
  }

  override fun handleCommand(interaction: ChatInputCommandInvocationInteraction): InteractionResponseCreateRequest {
    val birthday = getOptionByName("birthday", interaction)
    return InteractionResponseCreateRequest(
      InteractionResponseType.ChannelMessageWithSource,
      Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke("Reacted to birthday $birthday!")))
    )
  }
}
