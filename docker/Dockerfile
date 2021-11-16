FROM openjdk:8-jdk-alpine
ENV TZ Asia/Ho_Chi_Minh
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/target
RUN mkdir -p /home/upload/user
RUN mkdir -p /home/upload/game
RUN mkdir -p /home/upload/event
RUN mkdir -p /home/upload/tour
RUN mkdir -p /home/upload/news

ADD @project.build.finalName@.jar /usr/local/target/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
