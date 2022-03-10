package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Service that cleans up the database to remove staled data.
 *
 * @author Benjamin Eder
 */
@Service
class CleanupService(
  private val restClient: RestClient,
  private val guildConfigService: GuildConfigService,
) {

  private val log = LoggerFactory.getLogger(CleanupService::class.java)

  /**
   * Checks once per week the current configured guilds.
   * If a guild is configured the bot is no member anymore the configuration and birthdays associated with that guild will be deleted.
   */
  @Scheduled(cron = "0 0 3 * * SUN")
  fun cleanupDatabase() = runBlocking {
    log.info("Cleaning up stale guilds...")

    val configuredGuilds = guildConfigService.getAllGuildConfigs()
    val enrolledGuildIds = restClient.user.getCurrentUserGuilds()
      .map { it.id.toString() }

    val staleGuilds = configuredGuilds
      .filter { !enrolledGuildIds.contains(it.guildId) }
      .toTypedArray()

    guildConfigService.deleteGuildConfig(*staleGuilds)

    log.info("Deleted ${staleGuilds.size} stale guild configuration(s)! -> ${staleGuilds.map { it.guildId }}")
  }
}
