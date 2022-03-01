package io.github.srtigers98.birthdaydiscordbot.application.dao

import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Data access object to access the guild config entities in the database.
 *
 * @author Benjamin Eder
 */
@Repository
interface GuildConfigRepository : JpaRepository<GuildConfig, String>
