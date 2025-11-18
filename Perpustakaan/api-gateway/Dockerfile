FROM openjdk:17.0.1-jdk-oracle

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 9001

CMD ["java", "-jar", "app.jar"]
