package io.github.srtigers98.birthdaydiscordbot.application.service

import io.github.srtigers98.birthdaydiscordbot.application.dto.Birthday
import io.github.srtigers98.birthdaydiscordbot.application.dto.BirthdayId
import io.github.srtigers98.birthdaydiscordbot.application.dto.GuildConfig
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(GuildConfigService::class)
internal class GuildConfigServiceIntegrationTest {

  @Autowired
  private lateinit var tested: GuildConfigService

  @Autowired
  private lateinit var entityManager: TestEntityManager

  @Test
  fun deleteGuildConfigTest() {
    val guildConfig = GuildConfig("1", "101")
    val birthday = Birthday("42", guildConfig, "<@42>", 1, 1, 2000)

    entityManager.persist(guildConfig)
    entityManager.persist(birthday)

    tested.deleteGuildConfig(guildConfig)

    assertThat(entityManager.find(GuildConfig::class.java, "1"), `is`(nullValue()))
    assertThat(entityManager.find(Birthday::class.java, BirthdayId("42", "1")), `is`(nullValue()))
  }
}
