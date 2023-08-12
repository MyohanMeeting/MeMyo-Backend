FROM amazoncorretto:17 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME/
COPY gradle $APP_HOME/gradle/
RUN chmod +x gradlew
RUN ./gradlew -x test --info || return 0
COPY . .
RUN chmod +x gradlew
RUN ./gradlew -x test build

FROM amazoncorretto:17
ENV ARTIFACT_NAME=myoback.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT java -jar $ARTIFACT_NAME
