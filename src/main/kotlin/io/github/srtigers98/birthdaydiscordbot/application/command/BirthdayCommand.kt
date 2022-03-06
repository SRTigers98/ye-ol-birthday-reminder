package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.json.request.InteractionResponseCreateRequest

/**
 * A command for the bot that will be globally registered and can be executed through the users.
 *
 * @author Benjamin Eder
 */
abstract class BirthdayCommand(
  val name: String,
  val description: String,
  val builder: ChatInputCreateBuilder.() -> Unit
) {

  /**
   * Handles the incoming interaction and returns a response to the user.
   *
   * @param interaction interaction object with all request information
   * @return A response for the user
   */
  abstract fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest
}
