FROM adoptopenjdk/openjdk11:alpine AS build

WORKDIR /backend
 
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install

FROM adoptopenjdk/openjdk11:alpine

COPY --from=build /backend/target/backend.jar .
EXPOSE 8080

USER 1000:1000
ENTRYPOINT ["java","-jar","backend.jar"]
