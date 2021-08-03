# For Java 8
FROM openjdk:8-jdk-alpine

ARG JAR_FILE=./build/libs/shortened-url-service-0.0.1-SNAPSHOT.jar

# cd /url/app
WORKDIR /url/app

# cp build/libs/shortened-url-service-0.0.1-SNAPSHOT.jar /url/app/shortened-url-service.jar
COPY ${JAR_FILE} shortened-url-service.jar

# java -jar /url/app/shortened-url-service.jar
#-Dspring.profiles.active=develop argument i passed for development mode where log level is debug. If not needed simply remove this argument from below
ENTRYPOINT ["java","-Dspring.profiles.active=develop","-jar","shortened-url-service.jar"]
# For non-developemt mode comment line number 14 and enable line number 16 below
 # ENTRYPOINT ["java","-jar","shortened-url-service.jar"]