package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.Optional
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayInFutureException
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.springframework.stereotype.Component
import java.time.format.DateTimeParseException

private const val BIRTHDAY_KEY = "birthday"

@Component
class BirthdaySaveCommand(
  private val birthdayService: BirthdayService
) : BirthdayCommand("save", "Saves your birthday to the server.", {
  options = mutableListOf(
    StringChoiceBuilder(BIRTHDAY_KEY, "Your birthday.").apply {
      required = true
    }
  )
}) {

  override fun handleCommand(interaction: ChatInputCommandInteraction): InteractionResponseCreateRequest {
    val birthday = interaction.command.options.getValue(BIRTHDAY_KEY).value as String
    val response = saveBirthday(
      interaction.user.id.toString(),
      interaction.user.mention,
      interaction.data.guildId.value.toString(),
      interaction.channelId.toString(),
      birthday.trim()
    )

    return InteractionResponseCreateRequest(
      InteractionResponseType.ChannelMessageWithSource,
      Optional.invoke(InteractionApplicationCommandCallbackData(content = Optional.invoke(response)))
    )
  }

  private fun saveBirthday(
    userId: String,
    userMention: String,
    guildId: String,
    channelId: String,
    birthday: String
  ): String {
    if (!Regex("\\d{4}-\\d{2}-\\d{2}").matches(birthday)) {
      return """Invalid input for parameter **birthday** (*$birthday*)!
        |Your birthday was not saved, please try again!
      """.trimMargin()
    }

    return try {
      birthdayService.save(userId, userMention, guildId, channelId, birthday)
      "Hey $userMention, your birthday *$birthday* was saved successfully!"
    } catch (e: BirthdayInFutureException) {
      """The entered birthday *$birthday* is in the future!
        |Your birthday was **not** saved, please try again!""".trimMargin()
    } catch (e: DateTimeParseException) {
      """$userMention You entered an invalid date (*$birthday*)!
            |Your birthday was not saved!""".trimMargin()
    }
  }
}
