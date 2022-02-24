package io.github.srtigers98.birthdaydiscordbot.application.dao

import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.springframework.data.jpa.repository.JpaRepository

interface GuildConfigRepository : JpaRepository<GuildConfig, String>
