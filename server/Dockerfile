FROM amazoncorretto:11
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5081
ENV TZ="Europe/Moscow"
COPY target/*.jar server.jar
ENTRYPOINT ["java", "-jar", "/server.jar"]
