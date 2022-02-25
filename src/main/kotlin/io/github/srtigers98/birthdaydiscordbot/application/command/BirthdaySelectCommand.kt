package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.service.GuildConfigService
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

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

    return InteractionResponseCreateRequest(
      InteractionResponseType.ChannelMessageWithSource,
      Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke(response)))
    )
  }
}
