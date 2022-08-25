# syntax=docker/dockerfile:1

FROM java:8

# 声明容器对外提供的访问端口
#EXPOSE 9090

WORKDIR /app

#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline

ARG JAR_FILE

#COPY test-v1.0.jar ./java-docker.jar

ADD ${JAR_FILE} ./app.jar

#CMD ["./mvnw", "spring-boot:run"]

# ENTRYPOINT执行项目 java-docker.jar 为了缩短 Tomcat 启动时间 添加一个系统属性指向 "/dev/urandom" 作为 Entropy Source.

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]