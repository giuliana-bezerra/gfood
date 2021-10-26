echo Running migrations...
./gfood-flyway/mvnw clean flyway:migrate -f ./gfood-flyway/pom.xml

echo Building all projects...
./gfood-common/mvnw install -f ./gfood-common/pom.xml -Dmaven.test.skip=true
./gfood-domain/mvnw install -f ./gfood-domain/pom.xml -Dmaven.test.skip=true
./gfood-swagger/mvnw install -f ./gfood-swagger/pom.xml -Dmaven.test.skip=true
./gfood-consumer-service-api/mvnw install -f ./gfood-consumer-service-api/pom.xml -Dmaven.test.skip=true
./gfood-consumer-service/mvnw install -f ./gfood-consumer-service/pom.xml -Dmaven.test.skip=true
./gfood-application/mvnw install -f ./gfood-application/pom.xml -Dmaven.test.skip=true

echo Running gfood...
java -jar ./gfood-application/target/gfood-application-0.0.1-SNAPSHOT.jar