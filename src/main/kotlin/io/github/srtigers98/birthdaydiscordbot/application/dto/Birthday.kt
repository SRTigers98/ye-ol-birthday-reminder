package io.github.srtigers98.birthdaydiscordbot.application.dto

import jakarta.persistence.*

/**
 * Entity class to store the birthday of a guild member.
 *
 * @author Benjamin Eder
 */
@Entity
@IdClass(BirthdayId::class)
data class Birthday(
  @Id
  val userId: String,
  @Id
  @ManyToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "GUILD", referencedColumnName = "GUILD_ID")
  val guild: GuildConfig,
  val mention: String,
  val birthdayYear: Int,
  val birthdayMonth: Int,
  val birthdayDay: Int
)
