FROM openjdk:17
VOLUME /tmp
EXPOSE 8083
ARG JAR_FILE=target/rms-tasklist-workflow-backend-1.0.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]