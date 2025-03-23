@echo off
set USERNAME=shouqing777
echo Starting to push Docker images for %USERNAME%...

docker push %USERNAME%/eureka-server:latest
docker push %USERNAME%/config-server:latest
docker push %USERNAME%/api-gateway:latest
docker push %USERNAME%/product-service:latest
docker push %USERNAME%/order-service:latest

echo All images have been pushed for %USERNAME%!
pause