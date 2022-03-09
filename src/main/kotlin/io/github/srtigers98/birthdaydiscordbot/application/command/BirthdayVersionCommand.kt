package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Component

/**
 * Command to print the current version of the bot.
 *
 * @author Benjamin Eder
 */
@Component
class BirthdayVersionCommand(
  @Value("\${build.number:#{null}}") private val buildNumber: String?,
  @Value("\${build.hash:#{null}}") private val buildHash: String?,
  private val buildProperties: BuildProperties,
) : BirthdayCommand("version", "Prints the current version of the bot.", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val projectVersion = buildProperties.version

    val response = if (projectVersion.contains("SNAPSHOT")) {
      "$projectVersion - ${buildNumber ?: "dev"} (${buildHash ?: "local"})"
    } else {
      projectVersion
    }

    return this.createResponse(InteractionResponseType.ChannelMessageWithSource, response)
  }
}
