FROM openjdk:22-jdk

WORKDIR /app

COPY *.jar /app/myapp.jar

EXPOSE 8083

CMD ["java", "-jar", "myapp.jar"]
