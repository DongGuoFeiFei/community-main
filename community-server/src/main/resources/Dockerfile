FROM java:17

EXPOSE 8081

ADD community-server-0.0.1-SNAPSHOT.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=pro"]



#docker-compose stop community
#docker-compose rm -f community
#docker-compose build community
#docker-compose up -d

#docker-compose logs -f community

