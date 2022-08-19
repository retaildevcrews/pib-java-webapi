# ----- Base Java - Check Dependencies ----
#checkov:skip=CKV_DOCKER_2: No healthcheck is needed
FROM azul/zulu-openjdk-alpine:11.0.10 AS base
WORKDIR /app
ARG MAVEN_VERSION=3.6.3

# Install Maven
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xf apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    mv apache-maven-${MAVEN_VERSION}/ apache-maven/

ENV PATH=/app/apache-maven/bin:${PATH}

#
# ----Build App with Dependencies ----
FROM base AS dependencies
COPY . /app

#cache mount volume enabled for faster build
RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests --no-transfer-progress

#
# ---- Release App ----
FROM  azul/zulu-openjdk-alpine:11.0.10-jre AS release
WORKDIR /app

# Create the app user so we can run the app as non-root under app
RUN addgroup -g 4120 app && \
    adduser -u 4120 -G app -h /home/app -D app

USER app

COPY --from=dependencies /app/target/java-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java",  "-jar", "/app/app.jar"]
