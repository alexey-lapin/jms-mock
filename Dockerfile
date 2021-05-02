FROM openjdk:8-jre-alpine
COPY backend/service/build/libs/jms-mock.jar jms-mock.jar
EXPOSE 8080
CMD java ${JAVA_OPTS} -jar jms-mock.jar