package io.github.srtigers98.birthdaydiscordbot.application.dto

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class GuildConfig(
  @Id val guildId: String,
  val birthdayChannelId: String
)
