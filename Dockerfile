#Build stage
#Use the official Maven image as the base image
FROM maven:3.9.8-sapmachine-17 AS build
#Set the working directory
WORKDIR /build
#Copy the pom.xml file and install the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline
#Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

#Package stage
#Use JRE 17 to run the application
FROM sapmachine:17-jre-headless-ubuntu
#Set the working directory
WORKDIR /app
#Copy the JAR file from the build stage
COPY --from=build /build/target/viet-phuc-store-*.jar .
#Expose the port
EXPOSE 8082
#Run the application
ENTRYPOINT ["java", "-jar", "/app/viet-phuc-store-0.0.1-SNAPSHOT.jar"]