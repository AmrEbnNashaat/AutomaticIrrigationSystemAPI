FROM amazoncorretto:20-alpine-jdk
WORKDIR /app
COPY ../.mvn .mvn
COPY ../mvnw pom.xml ./
RUN ./mvnw -e dependency:go-offline
EXPOSE 8080
COPY ../src ./src
CMD ["./mvnw", "-e", "spring-boot:run"]
