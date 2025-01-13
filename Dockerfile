#Build stage
FROM maven:3.9.8-sapmachine-17 AS build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

#Package stage
FROM sapmachine:17-jre-headless-ubuntu

WORKDIR /app

COPY --from=build /build/target/viet-phuc-store-*.jar /app/app.jar
COPY src/main/resources/nextrad-dapvietbe.p12 /app/nextrad-dapvietbe.p12

EXPOSE 8082

#Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]