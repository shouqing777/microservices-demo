@echo off
set K8S_PATH=C:\Users\shouq\Desktop\microservices-demo

echo Applying Eureka Server configurations...
kubectl apply -f %K8S_PATH%\eureka-server\deployment.yaml -f %K8S_PATH%\eureka-server\service.yaml

echo Checking deployment status...
kubectl get deployments eureka-server

echo Checking service status...
kubectl get services eureka-server

echo Checking pods...
kubectl get pods -l app=eureka-server

pause