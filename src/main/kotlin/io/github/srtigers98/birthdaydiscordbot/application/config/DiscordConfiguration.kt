package io.github.srtigers98.birthdaydiscordbot.application.config

import dev.kord.core.Kord
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfiguration(
  @Value("\${bot.token}") private val token: String
) {

  @Bean
  fun kord(): Kord = runBlocking {
    return@runBlocking Kord(token)
  }

  @Bean
  fun restClient(): RestClient = RestClient(token)
}
