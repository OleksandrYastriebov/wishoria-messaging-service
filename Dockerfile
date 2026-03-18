FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/wishoria-messaging-service-1.0.0.jar"]