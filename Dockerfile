#assumption is Dockerfile is in project root
FROM openjdk:14-alpine

WORKDIR /app

COPY target/session-*.jar ./api.jar

EXPOSE 8080

RUN mkdir /dumps

CMD java -Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="/dumps/" -jar api.jar
