#assumption is Dockerfile is in project root
FROM openjdk:14-alpine

WORKDIR /app

COPY target/session-*.jar ./api.jar

EXPOSE 8080

RUN mkdir /dumps

CMD java -Xms128m -Xmx128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="/dumps/" -jar api.jar
