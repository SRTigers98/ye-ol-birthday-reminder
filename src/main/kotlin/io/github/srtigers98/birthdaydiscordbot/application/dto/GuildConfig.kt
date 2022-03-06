package io.github.srtigers98.birthdaydiscordbot.application.dto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Entity class for the configuration of a guild.
 *
 * @author Benjamin Eder
 */
@Entity
data class GuildConfig(
  @Id
  @Column(name = "GUILD_ID")
  val guildId: String,
  val birthdayChannelId: String
)
