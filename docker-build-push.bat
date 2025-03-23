@echo off
echo Building and pushing Docker images

set USERNAME=shouqing777

docker build -t %USERNAME%/eureka-server:latest ./eureka-server
docker push %USERNAME%/eureka-server:latest

docker build -t %USERNAME%/config-server:latest ./config-server
docker push %USERNAME%/config-server:latest

docker build -t %USERNAME%/api-gateway:latest ./api-gateway
docker push %USERNAME%/api-gateway:latest

docker build -t %USERNAME%/product-service:latest ./product-service
docker push %USERNAME%/product-service:latest

docker build -t %USERNAME%/order-service:latest ./order-service
docker push %USERNAME%/order-service:latest

echo All Docker images built and pushed
pause