package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.json.request.InteractionResponseCreateRequest

abstract class BirthdayCommand(
  val name: String,
  val description: String
) {

  abstract fun builder(): ChatInputCreateBuilder.() -> Unit

  abstract fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest
}
