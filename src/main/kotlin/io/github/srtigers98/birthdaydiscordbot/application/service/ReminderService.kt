package io.github.srtigers98.birthdaydiscordbot.application.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ReminderService {

  private val log: Logger = LoggerFactory.getLogger(ReminderService::class.java)

  @Scheduled(cron = "0 * * * * *")
  fun checkForBirthday() {
    log.info("Hello from the scheduler!")
  }
}
