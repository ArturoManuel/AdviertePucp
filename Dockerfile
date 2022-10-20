FROM openjdk:17.0.2-jdk
VOLUME /tmp
EXPOSE 8080
ADD ./target/AdviertePUCP-0.0.1-SNAPSHOT.jar adviertepucp.jar
ENTRYPOINT ["java","-jar","/adviertepucp.jar"]
