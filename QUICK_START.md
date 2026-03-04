# XunLu OAuth2 快速开始

## 🚀 5分钟快速启动

### 前置要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+
- Maven 3.8+

---

## 第一步：启动后端服务

### 1.1 数据库初始化

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE xunlu_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构和初始数据
mysql -u root -p xunlu_admin < sql/schema.sql
mysql -u root -p xunlu_admin < sql/data.sql
mysql -u root -p xunlu_admin < sql/oauth2_tables.sql
```

### 1.2 配置文件

**编辑**: `XunLuAdminCloud/oauth2-server/src/main/resources/application-prod.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xunlu_admin?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果有的话

# OAuth2 JWT密钥配置
oauth2:
  jwk:
    keystore-password: change-this-in-production
```

### 1.3 启动Nacos

```bash
cd nacos/bin
# Linux/Mac
./startup.sh -m standalone

# Windows
startup.cmd -m standalone
```

访问: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

### 1.4 启动网关和OAuth2服务

```bash
cd XunLuAdminCloud

# 启动网关
cd gateway
mvn spring-boot:run

# 启动OAuth2服务
cd ../oauth2-server
mvn spring-boot:run

# 启动System服务
cd ../services/system-service
mvn spring-boot:run
```

---

## 第二步：启动前端

```bash
cd frontend-cloud

# 安装依赖
npm install

# 启动开发服务器
npm start
```

访问: http://localhost:8000

**默认登录账号**:
- 用户名: admin
- 密码: admin123

---

## 第三步：验证OAuth2功能

### 3.1 内部管理后台登录测试

1. 打开浏览器访问 http://localhost:8000
2. 输入用户名和密码
3. 输入验证码
4. 点击登录

**查看Token**:
- 打开浏览器开发者工具
- 查看sessionStorage中的`xunlu_access_token`
- 查看Network请求，确认使用`Bearer Token`

### 3.2 OAuth2授权码流程测试（Postman）

**Step 1: 获取授权码**

在浏览器中访问:

```
http://localhost:9000/oauth2/authorize?
  response_type=code&
  client_id=demo-third-party-app&
  redirect_uri=https://oauth.pstmn.io/v1/callback&
  scope=openid%20profile&
  state=random_state_123&
  code_challenge=CHALLENGE&
  code_challenge_method=S256
```

登录后，用户授权同意，将重定向到:

```
https://oauth.pstmn.io/v1/callback?code=AUTHORIZATION_CODE&state=random_state_123
```

**Step 2: 交换Access Token**

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic ZGVtby10aGlyZC1wYXJ0eS1hcHA6ZGVtby1zZWNyZXQtY2hhbmdlLWluLXByb2R1Y3Rpb24=" \
  -d "grant_type=authorization_code&code=AUTHORIZATION_CODE&redirect_uri=https://oauth.pstmn.io/v1/callback&code_verifier=VERIFIER"
```

响应:

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 1800,
  "refresh_token": "...",
  "scope": "openid profile"
}
```

**Step 3: 获取用户信息**

```bash
curl -X GET http://localhost:9000/oauth2/userinfo \
  -H "Authorization: Bearer ACCESS_TOKEN"
```

响应:

```json
{
  "sub": "admin",
  "name": "管理员",
  "email": "admin@xunlu.com",
  "picture": "https://avatar.url"
}
```

---

## 常见启动问题

### 问题1: 端口被占用

```bash
# 查看端口占用
lsof -i:9000  # OAuth2服务端口
lsof -i:9001  # Gateway端口
lsof -i:8000  # 前端端口

# 修改端口
# 编辑 application.yml 中的 server.port
```

### 问题2: Nacos连接失败

检查Nacos配置:

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        namespace: prod  # 确保namespace存在
```

### 问题3: JWT密钥文件权限问题

```bash
# 创建密钥目录
mkdir -p config

# 设置权限
chmod 700 config
```

### 问题4: Redis连接失败

```bash
# 检查Redis是否启动
redis-cli ping  # 应返回PONG

# 检查Redis配置
redis-cli
127.0.0.1:6379> CONFIG GET requirepass
```

---

## 下一步

- [完整实施指南](./OAUTH2_IMPLEMENTATION_GUIDE.md)
- [第三方应用接入示例](./examples/third-party-app/)
- [API文档](http://localhost:9000/swagger-ui.html)
- [管理后台](http://localhost:8000)

---

## 技术支持

遇到问题？

1. 查看日志文件: `logs/oauth2-server.log`
2. GitHub Issues: https://github.com/your-org/XunLuAdmin/issues
3. 文档站点: https://docs.xunlu.com
