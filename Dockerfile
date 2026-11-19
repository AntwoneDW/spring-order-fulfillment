FROM eclipse-temurin:27-jre-ubi10-minimal

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]