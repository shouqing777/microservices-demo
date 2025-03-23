FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8882
ENTRYPOINT ["java", "-jar", "app.jar"]