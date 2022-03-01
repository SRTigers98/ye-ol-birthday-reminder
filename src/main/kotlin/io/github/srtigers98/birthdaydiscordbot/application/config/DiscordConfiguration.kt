package io.github.srtigers98.birthdaydiscordbot.application.config

import dev.kord.core.Kord
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for kord beans.
 *
 * @author Benjamin Eder
 */
@Configuration
class DiscordConfiguration(
  @Value("\${bot.token}") private val token: String
) {

  /**
   * Creates a kord instance to interact with the _Discord API_ with the configured token for the bot.
   */
  @Bean
  fun kord(): Kord = runBlocking {
    return@runBlocking Kord(token)
  }

  /**
   * Creates a rest client to interact with the _Discord REST API_ with the configured token for the bot.
   */
  @Bean
  fun restClient(): RestClient = RestClient(token)
}
