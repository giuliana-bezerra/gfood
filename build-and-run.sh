echo Running migrations...
./gfood-flyway/mvnw clean flyway:migrate -f ./gfood-flyway/pom.xml

echo Building all projects...
./gfood-common/mvnw clean install -f ./gfood-common/pom.xml -Dmaven.test.skip=true
./gfood-domain/mvnw clean install -f ./gfood-domain/pom.xml -Dmaven.test.skip=true
./gfood-swagger/mvnw clean install -f ./gfood-swagger/pom.xml -Dmaven.test.skip=true
./gfood-consumer-service-api/mvnw clean install -f ./gfood-consumer-service-api/pom.xml -Dmaven.test.skip=true
./gfood-consumer-service/mvnw clean install -f ./gfood-consumer-service/pom.xml -Dmaven.test.skip=true
./gfood-application/mvnw clean install -f ./gfood-application/pom.xml -Dmaven.test.skip=true

echo Running gfood...
java -jar ./gfood-application/target/gfood-application-0.0.1-SNAPSHOT.jar