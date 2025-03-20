# 微服務系統測試指南

本文檔提供了完整的微服務系統測試方法，包括測試前準備、各服務的API測試步驟以及常見問題排查。

## 目錄

1. [測試前準備](#測試前準備)
2. [服務啟動順序](#服務啟動順序)
3. [基礎設施服務測試](#基礎設施服務測試)
4. [商品服務 API 測試](#商品服務-api-測試)
5. [訂單服務 API 測試](#訂單服務-api-測試)
6. [整合場景測試](#整合場景測試)
7. [錯誤處理測試](#錯誤處理測試)
8. [使用 Postman 自動化測試](#使用-postman-自動化測試)
9. [常見問題排查](#常見問題排查)

<a id="測試前準備"></a>
## 1. 測試前準備

### 1.1 環境需求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- 可用的網絡連接
- Postman 或其他 API 測試工具

### 1.2 測試工具安裝

- 下載並安裝 [Postman](https://www.postman.com/downloads/)
- 或使用其他 API 測試工具如 [Insomnia](https://insomnia.rest/download)

<a id="服務啟動順序"></a>
## 2. 服務啟動順序

請按照以下順序啟動微服務：

1. **Eureka Server** (服務註冊中心)
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Config Server** (配置中心)
   ```bash
   cd config-server
   mvn spring-boot:run
   ```

3. **Product Service** (商品服務)
   ```bash
   cd product-service
   mvn spring-boot:run
   ```

4. **Order Service** (訂單服務)
   ```bash
   cd order-service
   mvn spring-boot:run
   ```

5. **API Gateway** (API閘道)
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

<a id="基礎設施服務測試"></a>
## 3. 基礎設施服務測試

### 3.1 Eureka Server 測試

1. 打開瀏覽器並訪問 Eureka 控制台：
   ```
   http://localhost:8761
   ```

2. 驗證以下服務已成功註冊：
   - API-GATEWAY
   - CONFIG-SERVER
   - PRODUCT-SERVICE
   - ORDER-SERVICE

### 3.2 Config Server 測試

測試配置是否可以正確獲取：

```
GET http://localhost:8888/product-service/default
```

預期結果：
- 狀態碼: 200 OK
- 返回 product-service 的配置內容

<a id="商品服務-api-測試"></a>
## 4. 商品服務 API 測試

> **注意**: 所有 API 請求都通過 API Gateway 發送 (`http://localhost:8080`)，而不是直接訪問服務。

### 4.1 創建商品

**請求**:

```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "iPhone 15 Pro",
  "description": "最新款 iPhone，搭載 A17 Pro 晶片",
  "price": 35900,
  "stock": 50,
  "category": "手機"
}
```

**預期結果**:
- 狀態碼: 201 Created
- 返回創建的商品信息，包含自動生成的 ID

**保存變量**:
- 保存返回的商品 ID 為 `productId1`

### 4.2 再創建一個商品

**請求**:

```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "AirPods Pro",
  "description": "主動降噪功能，無線充電",
  "price": 7990,
  "stock": 100,
  "category": "配件"
}
```

**預期結果**:
- 狀態碼: 201 Created
- 返回創建的商品信息

**保存變量**:
- 保存返回的商品 ID 為 `productId2`

### 4.3 獲取所有商品

**請求**:

```http
GET http://localhost:8080/api/products
```

**預期結果**:
- 狀態碼: 200 OK
- 返回商品列表，包含之前創建的兩個商品

### 4.4 獲取特定商品

**請求**:

```http
GET http://localhost:8080/api/products/{{productId1}}
```

**預期結果**:
- 狀態碼: 200 OK
- 返回特定 ID 的商品信息

### 4.5 按類別查詢商品

**請求**:

```http
GET http://localhost:8080/api/products/category/手機
```

**預期結果**:
- 狀態碼: 200 OK
- 返回類別為"手機"的所有商品

### 4.6 按價格範圍查詢商品

**請求**:

```http
GET http://localhost:8080/api/products/price-range?minPrice=7000&maxPrice=40000
```

**預期結果**:
- 狀態碼: 200 OK
- 返回價格介於 7000 到 40000 之間的商品

### 4.7 更新商品

**請求**:

```http
PUT http://localhost:8080/api/products/{{productId1}}
Content-Type: application/json

{
  "name": "iPhone 15 Pro",
  "description": "最新款 iPhone，搭載 A17 Pro 晶片，1TB 儲存空間",
  "price": 42900,
  "stock": 30,
  "category": "手機"
}
```

**預期結果**:
- 狀態碼: 200 OK
- 返回更新後的商品信息

### 4.8 更新商品庫存

**請求**:

```http
PATCH http://localhost:8080/api/products/{{productId1}}/stock?quantity=25
```

**預期結果**:
- 狀態碼: 200 OK
- 返回更新後的商品信息，庫存為 25

<a id="訂單服務-api-測試"></a>
## 5. 訂單服務 API 測試

### 5.1 創建訂單

**請求**:

```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "userId": 1,
  "items": [
    {
      "productId": {{productId1}},
      "quantity": 2
    },
    {
      "productId": {{productId2}},
      "quantity": 3
    }
  ]
}
```

**預期結果**:
- 狀態碼: 201 Created
- 返回創建的訂單信息，包含：
  - 訂單編號
  - 總金額
  - 訂單狀態為 "PENDING"
  - 商品詳情從商品服務中獲取並填充

**保存變量**:
- 保存返回的訂單 ID 為 `orderId1`

### 5.2 驗證商品庫存更新

**請求**:

```http
GET http://localhost:8080/api/products/{{productId1}}
```

**預期結果**:
- 狀態碼: 200 OK
- 商品庫存應該從 25 減少到 23

### 5.3 獲取所有訂單

**請求**:

```http
GET http://localhost:8080/api/orders
```

**預期結果**:
- 狀態碼: 200 OK
- 返回訂單列表，包含剛才創建的訂單

### 5.4 獲取特定訂單

**請求**:

```http
GET http://localhost:8080/api/orders/{{orderId1}}
```

**預期結果**:
- 狀態碼: 200 OK
- 返回特定 ID 的訂單信息

### 5.5 獲取特定用戶的訂單

**請求**:

```http
GET http://localhost:8080/api/orders/user/1
```

**預期結果**:
- 狀態碼: 200 OK
- 返回用戶 ID 為 1 的所有訂單

### 5.6 更新訂單狀態

**請求**:

```http
PATCH http://localhost:8080/api/orders/{{orderId1}}/status?status=PROCESSING
```

**預期結果**:
- 狀態碼: 200 OK
- 返回更新後的訂單信息，狀態為 "PROCESSING"

### 5.7 取消訂單

**請求**:

```http
POST http://localhost:8080/api/orders/{{orderId1}}/cancel
```

**預期結果**:
- 狀態碼: 204 No Content
- 沒有返回內容

### 5.8 驗證商品庫存恢復

**請求**:

```http
GET http://localhost:8080/api/products/{{productId1}}
```

**預期結果**:
- 狀態碼: 200 OK
- 商品庫存應該恢復到取消訂單前的數量 (25)

<a id="整合場景測試"></a>
## 6. 整合場景測試

### 6.1 完整訂單流程

這個測試場景模擬完整的訂單處理流程：

1. 創建新商品
2. 下訂單購買該商品
3. 更新訂單狀態為 PROCESSING
4. 更新訂單狀態為 SHIPPED
5. 更新訂單狀態為 DELIVERED

### 6.2 下單-取消流程

這個測試場景模擬訂單取消流程：

1. 檢查商品初始庫存
2. 下訂單購買該商品
3. 檢查庫存減少
4. 取消訂單
5. 驗證庫存恢復

<a id="錯誤處理測試"></a>
## 7. 錯誤處理測試

### 7.1 創建商品時提供無效數據

**請求**:

```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "",
  "price": -100,
  "stock": -10,
  "category": "測試"
}
```

**預期結果**:
- 狀態碼: 400 Bad Request
- 返回錯誤信息，指出名稱不能為空，價格和庫存不能為負數

### 7.2 獲取不存在的商品

**請求**:

```http
GET http://localhost:8080/api/products/9999
```

**預期結果**:
- 狀態碼: 404 Not Found
- 返回錯誤信息，表示找不到指定 ID 的商品

### 7.3 創建訂單時指定不存在的商品

**請求**:

```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "userId": 1,
  "items": [
    {
      "productId": 9999,
      "quantity": 1
    }
  ]
}
```

**預期結果**:
- 狀態碼: 404 Not Found
- 返回錯誤信息，指出找不到指定 ID 的商品

### 7.4 創建訂單時商品庫存不足

先將某個商品庫存設為很小的數量：

```http
PATCH http://localhost:8080/api/products/{{productId1}}/stock?quantity=5
```

然後嘗試訂購超過庫存的數量：

```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "userId": 1,
  "items": [
    {
      "productId": {{productId1}},
      "quantity": 10
    }
  ]
}
```

**預期結果**:
- 狀態碼: 400 Bad Request
- 返回錯誤信息，指出庫存不足

<a id="使用-postman-自動化測試"></a>
## 8. 使用 Postman 自動化測試

### 8.1 創建 Postman 集合

1. 打開 Postman
2. 創建新集合，命名為 "微服務系統測試"
3. 創建環境變量：
   - `gateway_url`: `http://localhost:8080`
   - `productId1`: (初始留空)
   - `productId2`: (初始留空)
   - `orderId1`: (初始留空)

### 8.2 創建測試腳本

在每個請求的 "Tests" 標籤中添加測試腳本：

**創建商品的測試腳本**:

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.test("商品名稱正確", function () {
    pm.expect(jsonData.name).to.eql("iPhone 15 Pro");
});

// 保存商品ID到環境變量
pm.environment.set("productId1", jsonData.id);
```

**創建訂單的測試腳本**:

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.test("訂單狀態為PENDING", function () {
    pm.expect(jsonData.status).to.eql("PENDING");
});

// 保存訂單ID到環境變量
pm.environment.set("orderId1", jsonData.id);
```

### 8.3 執行集合

使用 Postman 的 Collection Runner 功能，按順序執行所有請求，並查看測試結果。

<a id="常見問題排查"></a>
## 9. 常見問題排查

### 9.1 服務註冊問題

**問題**: 某些服務在 Eureka 中未顯示

**可能原因與解決方案**:
- 檢查服務的 `application.yml` 或 `bootstrap.yml` 中的 Eureka 配置
- 確保 `eureka.client.service-url.defaultZone` 設置正確
- 檢查網絡連接，確保服務可以連接到 Eureka Server

### 9.2 服務間通信問題

**問題**: 訂單服務無法調用商品服務

**可能原因與解決方案**:
- 檢查 Feign Client 的配置，確保 `@FeignClient` 注解中的服務名稱正確
- 檢查 Eureka 中的服務註冊情況
- 檢查商品服務是否正常運行
- 查看服務日誌以獲取更多信息

### 9.3 配置問題

**問題**: 服務未能從 Config Server 獲取配置

**可能原因與解決方案**:
- 檢查 Config Server 是否正常運行
- 確保 Config Server 可以連接到配置倉庫 (Git)
- 檢查服務的 bootstrap.yml 配置
- 確保配置文件命名符合規範 (`{application-name}.yml`)

### 9.4 HTTP 400/404 錯誤

**問題**: API 請求返回 400 Bad Request 或 404 Not Found

**可能原因與解決方案**:
- 檢查請求路徑是否正確
- 檢查請求參數或請求體是否符合要求
- 檢查 API Gateway 的路由配置
- 查看服務日誌以獲取更多信息

### 9.5 服務啟動失敗

**問題**: 服務無法啟動

**可能原因與解決方案**:
- 檢查依賴項是否正確
- 檢查配置文件語法
- 檢查端口是否已被佔用
- 查看啟動日誌以獲取更多信息

---

遵循本指南進行測試，可以全面驗證微服務系統的功能和穩定性。如有其他問題，請參考各服務的日誌或相關文檔。