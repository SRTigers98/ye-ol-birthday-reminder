package io.github.srtigers98.birthdaydiscordbot.application.run

import dev.kord.core.Kord
import io.github.srtigers98.birthdaydiscordbot.application.listener.KordListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Application runner to start the discord bot.
 *
 * @author Benjamin Eder
 */
@Component
class BotStartupRunner(
  @Qualifier("discord-scope") private val scope: CoroutineScope,
  private val kord: Kord,
  private val listener: List<KordListener<*>>,
) : ApplicationRunner {

  private val log: Logger = LoggerFactory.getLogger(BotStartupRunner::class.java)

  /**
   * Starts the discord bot.
   */
  override fun run(args: ApplicationArguments?) {
    // Setup listener
    runBlocking {
      val jobs = listener.map { it.register(kord) }
      log.info("${jobs.count()} listener(s) were set up!")
    }

    // Start the bot
    scope.launch { kord.login() }
    log.info("Discord Bot started!")
  }
}
