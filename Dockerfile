FROM openjdk:17 as builder

WORKDIR /opt/birthday-discord-bot

ADD . .

RUN chmod +x ./gradlew

RUN ./gradlew clean bootJar


FROM openjdk:17-alpine

WORKDIR /opt/birthday-discord-bot

COPY --from=builder /opt/birthday-discord-bot/build/libs/birthday-discord-bot.jar bot.jar

ENTRYPOINT ["java", "-jar", "bot.jar"]
