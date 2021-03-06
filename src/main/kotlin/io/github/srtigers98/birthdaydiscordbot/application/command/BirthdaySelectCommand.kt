package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.service.GuildConfigService
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

/**
 * Command to select the current channel as the output channel for the congratulation messages for this guild.
 *
 * @author Benjamin Eder
 */
@Component
class BirthdaySelectCommand(
  private val guildConfigService: GuildConfigService
) : BirthdayCommand("select", "Selects this channel for the congratulation messages.", {}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val guildId = interaction.data.guildId.value.toString()
    val channelId = interaction.channelId.toString()

    val hasManageGuildPermission = runBlocking {
      interaction.user.asMember(Snowflake(guildId))
        .getPermissions()
        .contains(Permission.ManageGuild)
    }

    val response = if (hasManageGuildPermission) {
      guildConfigService.selectGuildChannel(guildId, channelId)
      "Selected **this** channel as output channel for the congratulation messages!"
    } else {
      "${interaction.user.mention} You're **not allowed** to change the congratulation channel!"
    }

    return this.createResponse(InteractionResponseType.ChannelMessageWithSource, response)
  }
}
