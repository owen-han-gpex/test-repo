export $(cat .env | xargs)
#java -jar -Dspring.profiles.active=qa gpex-front-api-1.0-SNAPSHOT.jar
#java -jar -Dspring.profiles.active=qa gpex-scheduler-1.0-SNAPSHOT.jar
#java -jar -Dspring.profiles.active=develop gpex-admin-api-1.0-SNAPSHOT.jar & java -jar -Dspring.profiles.active=develop gpex-scheduler-1.0-SNAPSHOT.jar
java -jar -Dspring.profiles.active=dev gpex-admin-api-1.0-SNAPSHOT.jar
