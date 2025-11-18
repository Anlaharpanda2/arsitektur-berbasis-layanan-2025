FROM openjdk:17.0.1-jdk-oracle

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8087

CMD ["java", "-jar", "app.jar"]
