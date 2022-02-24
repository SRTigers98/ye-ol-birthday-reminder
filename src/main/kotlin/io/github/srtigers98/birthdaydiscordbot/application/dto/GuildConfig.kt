package io.github.srtigers98.birthdaydiscordbot.application.dto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class GuildConfig(
  @Id
  @Column(name = "GUILD_ID")
  val guildId: String,
  val birthdayChannelId: String
)
