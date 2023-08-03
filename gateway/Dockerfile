FROM amazoncorretto:11
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5082
ENV TZ="Europe/Moscow"
COPY target/*.jar gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]