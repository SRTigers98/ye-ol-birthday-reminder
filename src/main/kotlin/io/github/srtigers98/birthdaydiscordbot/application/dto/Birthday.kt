package io.github.srtigers98.birthdaydiscordbot.application.dto

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
  val birthdayYear: Int,
  val birthdayMonth: Int,
  val birthdayDay: Int
)
