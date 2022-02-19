package io.github.srtigers98.birthdaydiscordbot.application.dto

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
@IdClass(BirthdayId::class)
data class Birthday(
  @Id
  val userId: String,
  @Id
  val channelId: String,
  val mention: String,
  val birthday: LocalDate
)
