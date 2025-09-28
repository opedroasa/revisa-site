# ===== build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src

# força UTF-8 no build
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN mvn -q -DskipTests \
  -Dproject.build.sourceEncoding=UTF-8 \
  -Dproject.reporting.outputEncoding=UTF-8 \
  -Dfile.encoding=UTF-8 package

# ===== run =====
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75" PORT=8080
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]