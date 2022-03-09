package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.boot.info.BuildProperties

internal class BirthdayVersionCommandTest {

  private val buildProperties: BuildProperties = mock()

  @Test
  fun handleCommandTest() {
    val tested = BirthdayVersionCommand("99", "sha256", buildProperties)

    val interaction: ChatInputCommandInteraction = mock()

    whenever(buildProperties.version)
      .thenReturn("1.0.0")

    val response = tested.handleCommand(interaction)

    assertThat(response, `is`(notNullValue()))
    assertThat(response.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(response.data.value, `is`(notNullValue()))
    assertThat(response.data.value?.content?.value, `is`("1.0.0"))
  }

  @Test
  fun handleCommandSnapshotVersionTest() {
    val tested = BirthdayVersionCommand("99", "sha256", buildProperties)

    val interaction: ChatInputCommandInteraction = mock()

    whenever(buildProperties.version)
      .thenReturn("1.0.0-SNAPSHOT")

    val response = tested.handleCommand(interaction)

    assertThat(response, `is`(notNullValue()))
    assertThat(response.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(response.data.value, `is`(notNullValue()))
    assertThat(response.data.value?.content?.value, `is`("1.0.0-SNAPSHOT - 99 (sha256)"))
  }

  @Test
  fun handleCommandSnapshotWithoutBuildTest() {
    val tested = BirthdayVersionCommand(null, null, buildProperties)

    val interaction: ChatInputCommandInteraction = mock()

    whenever(buildProperties.version)
      .thenReturn("1.0.0-SNAPSHOT")

    val response = tested.handleCommand(interaction)

    assertThat(response, `is`(notNullValue()))
    assertThat(response.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(response.data.value, `is`(notNullValue()))
    assertThat(response.data.value?.content?.value, `is`("1.0.0-SNAPSHOT - dev (local)"))
  }
}
