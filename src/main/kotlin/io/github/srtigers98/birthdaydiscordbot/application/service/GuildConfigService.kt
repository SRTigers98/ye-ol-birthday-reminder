package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.GuildConfigRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.springframework.stereotype.Service

@Service
class GuildConfigService(
  private val guildConfigRepository: GuildConfigRepository
) {

  fun selectGuildChannel(guildId: String, channelId: String): GuildConfig {
    val guildConfig = GuildConfig(guildId, channelId)
    return guildConfigRepository.save(guildConfig)
  }

  fun getGuildConfig(guildId: String, currentChannelId: String): GuildConfig =
    guildConfigRepository.findById(guildId)
      .orElseGet { GuildConfig(guildId, currentChannelId) }
}
