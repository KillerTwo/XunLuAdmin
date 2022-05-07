FROM openjdk:17.0.2-jdk
ARG JAR_FILE=./*.jar
ARG CONF_FILE=./application.yml
COPY ${JAR_FILE} app.jar
COPY ${CONF_FILE} application.yml
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=prod"]