FROM eclipse-temurin:17-jdk-alpine AS build

ENV WORK_DIR=/build
# copy mvn wrapper and pom.xml file
COPY .mvn ${WORK_DIR}/.mvn
COPY mvnw ${WORK_DIR}/mvnw
COPY pom.xml ${WORK_DIR}/pom.xml
# copy rest of files
COPY / ${WORK_DIR}/

WORKDIR $WORK_DIR
# build all dependencies for offline use / cache them for subsequent builds
RUN ${WORK_DIR}/mvnw dependency:go-offline --file ${WORK_DIR}/pom.xml

RUN mkdir -p /usr/k0le/release-tracker-service
RUN ${WORK_DIR}/mvnw -DskipTests=true --file ${WORK_DIR}/pom.xml clean install
RUN cp ${WORK_DIR}/release-tracker-app/target/release-tracker-service.jar /usr/k0le/release-tracker-service/release-tracker-service.jar
###############
# Final build #
###############
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /usr/k0le/release-tracker-service/release-tracker-service.jar .
RUN addgroup --system k0le \
    && adduser --system --ingroup k0le k0le
# Update and install needed packages
RUN apk update \
    && apk add --no-cache curl \
    && rm -rf /var/cache/apk/*
# Add wait script
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait
USER k0le
CMD /wait && java -jar release-tracker-service.jar

