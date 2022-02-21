package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.optional.firstOrNull
import dev.kord.common.entity.optional.map
import dev.kord.core.entity.interaction.ChatInputCommandInvocationInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.json.request.InteractionResponseCreateRequest

abstract class BirthdayCommand(
  val name: String,
  val description: String
) {

  abstract fun builder(): ChatInputCreateBuilder.() -> Unit

  abstract fun handleCommand(interaction: ChatInputCommandInvocationInteraction): InteractionResponseCreateRequest

  fun checkCommand(interaction: ChatInputCommandInvocationInteraction): Boolean {
    return interaction.data.data.name.map { it == this.name }.value == true
  }

  protected fun getOptionByName(name: String, interaction: ChatInputCommandInvocationInteraction): Any? {
    val option = interaction.data.data.options.firstOrNull { it.value.value?.name == name }
    return option?.value?.value?.value
  }
}
