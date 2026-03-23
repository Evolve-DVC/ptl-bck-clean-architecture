FROM amazoncorretto:21-alpine
VOLUME /tmp

ARG ARTIFACT
COPY application/build/libs/${ARTIFACT} ${ARTIFACT}

# Define la variable de entorno SPRING_PROFILE y establece su valor al argumento de construcción SPRING_PROFILE
ARG SPRING_PROFILE
ENV SPRING_PROFILE=$SPRING_PROFILE
ENV ARTIFACT_NAME=${ARTIFACT}

# Muestra el valor de la variable de entorno durante la construcción (opcional)
RUN echo "Spring profile: $SPRING_PROFILE"

#Setear la hora
RUN apk update --no-cache && apk add --no-cache tzdata
ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/"$TZ" /etc/localtime && echo "$TZ" > /etc/timezone


ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar $ARTIFACT_NAME --server.port=8080 --spring.profiles.active=${SPRING_PROFILE}" ]
