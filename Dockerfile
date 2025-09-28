# ===== build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# cache de dependências
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ===== run =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# copia o jar gerado (qualquer nome/versão)
COPY --from=build /app/target/*.jar app.jar

# Render usa PORT; passamos pro Spring Boot
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75"
ENV PORT=8080
EXPOSE 8080

CMD ["sh","-c","java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
