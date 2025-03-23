@echo off
cd eureka-server
call mvn clean package -DskipTests
cd ..

cd config-server
call mvn clean package -DskipTests
cd ..

cd api-gateway
call mvn clean package -DskipTests
cd ..

cd product-service
call mvn clean package -DskipTests
cd ..

cd order-service
call mvn clean package -DskipTests
cd ..

echo All services built successfully
pause