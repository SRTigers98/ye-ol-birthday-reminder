package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.core.entity.Message
import io.github.srtigers98.birthdaydiscordbot.application.dao.BirthdayRepository
import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class BirthdayService(
  private val birthdayRepository: BirthdayRepository
) {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  fun save(msg: Message, birthdayInput: String) {
    val userId = msg.author?.id.toString()
    val mention = msg.author?.mention as String
    val channelId = msg.channelId.toString()

    val birthdayDate = LocalDate.parse(birthdayInput, formatter)

    val birthday = Birthday(
      userId,
      channelId,
      mention,
      birthdayDate
    )

    birthdayRepository.save(birthday)
  }
}
