package io.github.srtigers98.birthdaydiscordbot.application.listener

import dev.kord.core.Kord
import dev.kord.core.event.Event
import kotlinx.coroutines.Job

/**
 * Interface for all kord listeners.
 *
 * @author Benjamin Eder
 */
interface KordListener<in T : Event> {

  /**
   * Creates the consumer which consumes the incoming event.
   *
   * @return event consumer
   */
  fun consumer(): suspend T.() -> Unit

  /**
   * Registers the listener to the given kord instance.
   *
   * @param kord the kord instance
   * @return the coroutine job
   */
  suspend fun register(kord: Kord): Job
}
