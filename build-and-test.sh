echo Running migrations...
./gfood-flyway/mvnw clean flyway:migrate -f ./gfood-flyway/pom.xml

echo Building and running end to end tests...
./gfood-end-to-end-tests/mvnw install -f ./gfood-end-to-end-tests/pom.xml

echo Building all projects...
./gfood-common/mvnw install -f ./gfood-common/pom.xml
./gfood-domain/mvnw install -f ./gfood-domain/pom.xml
./gfood-swagger/mvnw install -f ./gfood-swagger/pom.xml
./gfood-consumer-service-api/mvnw install -f ./gfood-consumer-service-api/pom.xml
./gfood-consumer-service/mvnw install -f ./gfood-consumer-service/pom.xml
./gfood-restaurant-service-api/mvnw install -f ./gfood-restaurant-service-api/pom.xml
./gfood-restaurant-service/mvnw install -f ./gfood-restaurant-service/pom.xml
./gfood-courier-service-api/mvnw install -f ./gfood-courier-service-api/pom.xml
./gfood-courier-service/mvnw install -f ./gfood-courier-service/pom.xml
./gfood-order-service-api/mvnw install -f ./gfood-order-service-api/pom.xml
./gfood-order-service/mvnw install -f ./gfood-order-service/pom.xml
./gfood-application/mvnw install -f ./gfood-application/pom.xml