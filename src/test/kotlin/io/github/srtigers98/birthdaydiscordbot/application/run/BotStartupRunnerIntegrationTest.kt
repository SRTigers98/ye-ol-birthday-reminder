package io.github.srtigers98.birthdaydiscordbot.application.run

import dev.kord.core.Kord
import kotlinx.coroutines.isActive
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BotStartupRunnerIntegrationTest {

  @Autowired
  private lateinit var kord: Kord

  @Test
  fun startupTest() {
    // Make sure the application is starting
    assertThat(kord.isActive, `is`(true))
  }
}
