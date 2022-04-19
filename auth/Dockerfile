# Pull base image
FROM  java:8

VOLUME /tmp

# 添加
ADD auth-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'


# Define default command.
ENTRYPOINT ["java","-D spring.profiles.active=prod","-jar","/app.jar"]

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone