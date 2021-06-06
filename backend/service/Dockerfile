FROM openjdk:8-jre-alpine as builder
WORKDIR /app
COPY build/libs/jms-mock-*.jar jms-mock.jar
RUN java -Djarmode=layertools -jar jms-mock.jar extract

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
EXPOSE 8080
CMD java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher