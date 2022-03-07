package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dao.GuildConfigRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service to manage the guild configurations.
 *
 * @author Benjamin Eder
 */
@Service
class GuildConfigService(
  private val guildConfigRepository: GuildConfigRepository
) {

  private val log: Logger = LoggerFactory.getLogger(GuildConfigService::class.java)

  /**
   * Saves the given channel as the birthday channel for the given guild.
   * Creates a guild config and saves it to the database.
   *
   * @param guildId id of the guild
   * @param channelId id of the intended birthday channel
   * @return The saved guild config
   */
  fun selectGuildChannel(guildId: String, channelId: String): GuildConfig {
    log.info("Setting guild channel for guild {} to channel {}", guildId, channelId)

    val guildConfig = GuildConfig(guildId, channelId)
    return guildConfigRepository.save(guildConfig)
  }

  /**
   * Gets the guild configuration for the given guild.
   * If no guild config exists for this guild, then a new guild config with the given channel will be created.
   *
   * @param guildId id of the guild
   * @param currentChannelId id if the currently used channel
   * @return The stored or newly created guild config
   */
  fun getGuildConfig(guildId: String, currentChannelId: String): GuildConfig =
    guildConfigRepository.findById(guildId)
      .orElseGet { GuildConfig(guildId, currentChannelId) }
}
