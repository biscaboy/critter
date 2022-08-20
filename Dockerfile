# Pre-spring-boot 3.2 Dockerfile
# This Dockerfile creates one big layer because
# this project is not Spring 3.2 which contains
# the build tools for layered images.Dockerfile

# Get Maven image and name it "maven" for later use
FROM maven:3.6.3-jdk-11 as maven

# copy the files for the build to the maven container
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# package up the jar without running tests
RUN mvn -DskipTests package

# Source: https://reflectoring.io/spring-boot-docker/
FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY --from=maven ${JAR_FILE} application.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/application.jar"]