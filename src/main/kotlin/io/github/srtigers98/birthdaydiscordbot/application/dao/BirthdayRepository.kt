package io.github.srtigers98.birthdaydiscordbot.application.dao

import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.BirthdayId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BirthdayRepository : JpaRepository<Birthday, BirthdayId> {

  fun findByBirthdayMonthIsAndBirthdayDayIs(month: Int, day: Int): List<Birthday>
}
