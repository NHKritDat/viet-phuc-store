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

COPY --from=build /build/target/viet-phuc-store-*.jar .

EXPOSE 8082

ARG APP_VERSION=0.0.1-SNAPSHOT
ENV JAR_VERSION=${APP_VERSION}

#Run the application
ENTRYPOINT ["sh", "-c", "java -jar /app/viet-phuc-store-${JAR_VERSION}.jar"]