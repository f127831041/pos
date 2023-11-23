FROM openjdk:8-alpine
EXPOSE 8086
ADD pos.jar pos.jar
ENTRYPOINT ["java","-jar","pos.jar"]