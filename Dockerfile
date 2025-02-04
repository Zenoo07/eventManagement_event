FROM openjdk:11
EXPOSE 8091
ADD target/event-1.0.jar event-1.0.jar
ENTRYPOINT ["java","-jar","/event-1.0.jar"]