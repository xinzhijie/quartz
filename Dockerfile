# FROM java:8-jre-alpine
FROM 10.27.213.66:5000/20180528jre8:latest
VOLUME /tmp
ADD target/*.jar app.jar
RUN sh -c 'touch /app.jar' \
	&& echo $(date) > /image_built_at 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=${SPRING_PROFILES_ACTIVE:default}"]
