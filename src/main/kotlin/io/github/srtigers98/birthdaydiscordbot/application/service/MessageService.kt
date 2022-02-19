package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.core.entity.Message
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayInFutureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MessageService(
  private val birthdayService: BirthdayService
) {

  private val log: Logger = LoggerFactory.getLogger(MessageService::class.java)

  suspend fun handleMessage(message: Message) {
    if (Regex("!bdayIs \\d{4}-\\d{2}-\\d{2}").matches(message.content)) {
      try {
        birthdayService.save(message, message.content.split(" ")[1])
        log.info("Birthday for user ${message.author?.username} saved successfully!")
        message.channel.createMessage("Hey ${message.author?.mention}, your birthday was saved successfully!")
      } catch (e: BirthdayInFutureException) {
        e.message?.let {
          log.warn(it)
          message.channel.createMessage("${message.author?.mention} $it")
        }
      }
    }
  }
}
