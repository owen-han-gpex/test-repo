FROM gradle:7.6-jdk11 as gradleimage
COPY . /home/gradle/source
RUN chmod +x /home/gradle/source/run.sh
WORKDIR /home/gradle/source
RUN gradle :components:projects:gpex-app-api:build
#RUN gradle build

FROM eclipse-temurin:11
COPY --from=gradleimage /home/gradle/source/run.sh /app/
COPY --from=gradleimage /home/gradle/source/.env /app/
COPY --from=gradleimage /home/gradle/source/components/projects/gpex-app-api/build/libs/*.jar /app/
#COPY --from=gradleimage /home/gradle/source/components/projects/gpex-admin-api/build/libs/*.jar /app/
#COPY --from=gradleimage /home/gradle/source/components/projects/gpex-scheduler/build/libs/*.jar /app/
WORKDIR /app
ENTRYPOINT ["bash", "run.sh"]
