FROM openjdk:18 as builder

WORKDIR /opt/birthday-discord-bot

ADD . .

RUN chmod +x ./gradlew

RUN ./gradlew clean bootJar


FROM openjdk:18-alpine

WORKDIR /opt/birthday-discord-bot

COPY --from=builder /opt/birthday-discord-bot/build/libs/birthday-discord-bot.jar bot.jar

ARG buildNumber=dev
ARG buildHash=docker

ENV SPRING_PROFILES_ACTIVE prod
ENV BUILD_NUMBER $buildNumber
ENV BUILD_HASH $buildHash

ENTRYPOINT ["java", "-jar", "bot.jar"]
