# OAuth2 客户端新增功能使用指南

## 概述

`Oauth2RegisteredClientServiceImpl.insertOauth2RegisteredClient()` 方法已完善，支持完整的 OAuth2 客户端注册功能。

## 功能特性

### ✅ 已实现的功能

1. **完整的参数校验**
   - 客户端ID、名称、授权类型必填校验
   - 客户端ID唯一性检查

2. **灵活的客户端认证配置**
   - 支持多种认证方法：NONE、CLIENT_SECRET_BASIC、CLIENT_SECRET_POST、CLIENT_SECRET_JWT、PRIVATE_KEY_JWT
   - 自动判断：有密钥使用 BASIC，无密钥使用 NONE

3. **全面的授权类型支持**
   - authorization_code（授权码模式）
   - refresh_token（刷新令牌）
   - client_credentials（客户端凭证模式）
   - password（密码模式）

4. **智能默认配置**
   - ClientSettings 智能默认值
   - TokenSettings 合理默认值
   - JSON 配置解析支持

5. **详细的日志记录**
   - 操作日志
   - 错误日志
   - 警告信息

## 使用方法

### 1. 基本用法（最少参数）

```java
Oauth2RegisteredClient client = new Oauth2RegisteredClient();
client.setClientId("my-app");
client.setClientName("我的应用");
client.setClientSecret("my-secret");  // 可选，无密钥则为公共客户端
client.setAuthorizationGrantTypes("authorization_code,refresh_token");
client.setScopes("user.read,user.write");

int result = oauth2RegisteredClientService.insertOauth2RegisteredClient(client);
```

**说明**:
- 未指定的配置将使用智能默认值
- Access Token 默认 2 小时
- Refresh Token 默认 7 天

### 2. 完整配置（所有参数）

```java
Oauth2RegisteredClient client = new Oauth2RegisteredClient();

// 基本信息
client.setClientId("my-complete-app");
client.setClientName("完整配置应用");
client.setClientSecret("my-secure-secret");

// 认证方法（多个用逗号分隔）
client.setClientAuthenticationMethods("client_secret_basic,client_secret_post");

// 授权类型（多个用逗号分隔）
client.setAuthorizationGrantTypes("authorization_code,refresh_token");

// 重定向URI（多个用逗号分隔）
client.setRedirectUris("http://localhost:8080/callback,https://app.example.com/oauth/callback");

// Scopes（多个用逗号分隔）
client.setScopes("openid,profile,email,user.read,user.write");

// ClientSettings（JSON 格式）
String clientSettings = """
{
  "requireAuthorizationConsent": true,
  "requireProofKey": false
}
""";
client.setClientSettings(clientSettings);

// TokenSettings（JSON 格式）
String tokenSettings = """
{
  "accessTokenTimeToLive": 3600,
  "refreshTokenTimeToLive": 2592000,
  "reuseRefreshTokens": false,
  "accessTokenFormat": "SELF_CONTAINED"
}
""";
client.setTokenSettings(tokenSettings);

int result = oauth2RegisteredClientService.insertOauth2RegisteredClient(client);
```

### 3. 不同客户端类型的示例

#### 第三方应用（授权码模式）

```java
Oauth2RegisteredClient thirdPartyApp = new Oauth2RegisteredClient();
thirdPartyApp.setClientId("third-party-app");
thirdPartyApp.setClientName("第三方应用");
thirdPartyApp.setClientSecret("secret-12345");
thirdPartyApp.setClientAuthenticationMethods("client_secret_basic");
thirdPartyApp.setAuthorizationGrantTypes("authorization_code,refresh_token");
thirdPartyApp.setRedirectUris("https://app.example.com/callback");
thirdPartyApp.setScopes("user.read");

// 需要用户授权同意
thirdPartyApp.setClientSettings("{\"requireAuthorizationConsent\": true, \"requireProofKey\": true}");

// Token 有效期：30分钟 / 30天
thirdPartyApp.setTokenSettings("{\"accessTokenTimeToLive\": 1800, \"refreshTokenTimeToLive\": 2592000}");

oauth2RegisteredClientService.insertOauth2RegisteredClient(thirdPartyApp);
```

#### 内部管理后台（密码模式 - 公共客户端）

```java
Oauth2RegisteredClient adminClient = new Oauth2RegisteredClient();
adminClient.setClientId("admin-web");
adminClient.setClientName("管理后台");
// 不设置 clientSecret，将自动使用 NONE 认证方法
adminClient.setAuthorizationGrantTypes("password,refresh_token");
adminClient.setScopes("openid,profile,email,all");

// 不需要用户授权
adminClient.setClientSettings("{\"requireAuthorizationConsent\": false, \"requireProofKey\": false}");

// Token 有效期：2小时 / 7天
adminClient.setTokenSettings("{\"accessTokenTimeToLive\": 7200, \"refreshTokenTimeToLive\": 604800}");

oauth2RegisteredClientService.insertOauth2RegisteredClient(adminClient);
```

#### 微服务（客户端凭证模式）

```java
Oauth2RegisteredClient microservice = new Oauth2RegisteredClient();
microservice.setClientId("microservice-internal");
microservice.setClientName("内部微服务");
microservice.setClientSecret("microservice-secret");
microservice.setClientAuthenticationMethods("client_secret_basic");
microservice.setAuthorizationGrantTypes("client_credentials");
microservice.setScopes("service.call,internal.api");

// 不需要用户授权
microservice.setClientSettings("{\"requireAuthorizationConsent\": false}");

// Token 有效期：1小时（无需 refresh token）
microservice.setTokenSettings("{\"accessTokenTimeToLive\": 3600}");

oauth2RegisteredClientService.insertOauth2RegisteredClient(microservice);
```

#### 移动应用（PKCE）

```java
Oauth2RegisteredClient mobileApp = new Oauth2RegisteredClient();
mobileApp.setClientId("mobile-app");
mobileApp.setClientName("移动应用");
// 移动应用不使用密钥（公共客户端）
mobileApp.setAuthorizationGrantTypes("authorization_code,refresh_token");
mobileApp.setRedirectUris("myapp://callback");
mobileApp.setScopes("user.read,user.write");

// 强制使用 PKCE
mobileApp.setClientSettings("{\"requireAuthorizationConsent\": true, \"requireProofKey\": true}");

// Token 有效期：1小时 / 30天
mobileApp.setTokenSettings("{\"accessTokenTimeToLive\": 3600, \"refreshTokenTimeToLive\": 2592000}");

oauth2RegisteredClientService.insertOauth2RegisteredClient(mobileApp);
```

## 字段说明

### 必填字段

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| clientId | String | 客户端ID（唯一） | "my-app" |
| clientName | String | 客户端名称 | "我的应用" |
| authorizationGrantTypes | String | 授权类型（逗号分隔） | "authorization_code,refresh_token" |

### 可选字段

| 字段 | 类型 | 说明 | 默认值 |
|------|------|------|--------|
| clientSecret | String | 客户端密钥 | 无（公共客户端） |
| clientAuthenticationMethods | String | 认证方法（逗号分隔） | 有密钥：client_secret_basic<br>无密钥：none |
| redirectUris | String | 重定向URI（逗号分隔） | 无 |
| scopes | String | 授权范围（逗号分隔） | openid（自动添加） |
| clientSettings | String | 客户端设置（JSON） | 见下方 |
| tokenSettings | String | Token设置（JSON） | 见下方 |

### ClientSettings（JSON 格式）

```json
{
  "requireAuthorizationConsent": true,  // 是否需要用户授权同意（默认：授权码模式为true）
  "requireProofKey": false              // 是否需要PKCE（默认：公共客户端为true）
}
```

### TokenSettings（JSON 格式）

```json
{
  "accessTokenTimeToLive": 7200,        // Access Token 有效期（秒，默认7200=2小时）
  "refreshTokenTimeToLive": 604800,     // Refresh Token 有效期（秒，默认604800=7天）
  "reuseRefreshTokens": false,          // 是否重用 Refresh Token（默认false）
  "accessTokenFormat": "SELF_CONTAINED" // Token格式：SELF_CONTAINED(JWT) 或 REFERENCE（默认JWT）
}
```

## 授权类型说明

| 授权类型 | 字符串值 | 适用场景 | 是否需要密钥 |
|----------|---------|----------|-------------|
| 授权码模式 | authorization_code | 第三方应用、Web应用 | 推荐 |
| 刷新令牌 | refresh_token | 配合其他授权类型使用 | 同主授权类型 |
| 客户端凭证 | client_credentials | 微服务、后台服务 | 必须 |
| 密码模式 | password | 内部可信应用（不推荐） | 可选 |

## 客户端认证方法说明

| 认证方法 | 字符串值 | 说明 |
|----------|---------|------|
| 无认证 | none | 公共客户端（如移动应用） |
| Basic认证 | client_secret_basic | 密钥放在HTTP Basic认证头中 |
| POST认证 | client_secret_post | 密钥放在请求体中 |
| JWT认证 | client_secret_jwt | 使用密钥签名的JWT |
| 私钥JWT | private_key_jwt | 使用私钥签名的JWT |

## 智能默认值

### 认证方法默认值

- **有 clientSecret**: 使用 `client_secret_basic`
- **无 clientSecret**: 使用 `none`（公共客户端）

### ClientSettings 默认值

- **requireAuthorizationConsent**:
  - 授权码模式：`true`（需要用户授权）
  - 其他模式：`false`（不需要）

- **requireProofKey**:
  - 公共客户端：`true`（强制PKCE）
  - 机密客户端：`false`（可选）

### TokenSettings 默认值

- **accessTokenTimeToLive**: 7200秒（2小时）
- **refreshTokenTimeToLive**: 604800秒（7天）
- **reuseRefreshTokens**: false（不重用）
- **accessTokenFormat**: SELF_CONTAINED（JWT）

## 错误处理

### 常见错误

1. **客户端ID已存在**
   ```
   错误信息: 客户端ID已存在: xxx
   解决方法: 使用不同的客户端ID或先删除现有客户端
   ```

2. **必填字段为空**
   ```
   错误信息: 客户端ID不能为空
   解决方法: 设置必填字段
   ```

3. **JSON 格式错误**
   ```
   错误信息: 解析 clientSettings 失败
   解决方法: 检查JSON格式是否正确，使用默认配置
   ```

### 异常处理建议

```java
try {
    int result = oauth2RegisteredClientService.insertOauth2RegisteredClient(client);
    if (result > 0) {
        log.info("客户端注册成功: {}", client.getClientId());
    }
} catch (IllegalArgumentException e) {
    // 参数校验失败
    log.error("参数错误: {}", e.getMessage());
    return AjaxResult.error("参数错误: " + e.getMessage());
} catch (RuntimeException e) {
    // 业务异常（如客户端ID已存在）
    log.error("注册失败: {}", e.getMessage());
    return AjaxResult.error("注册失败: " + e.getMessage());
} catch (Exception e) {
    // 未知异常
    log.error("系统错误", e);
    return AjaxResult.error("系统错误，请联系管理员");
}
```

## 日志输出

### 成功日志

```
INFO  客户端注册成功: clientId=my-app, clientName=我的应用
```

### 警告日志

```
WARN  不支持的客户端认证方法: unknown_method
WARN  不支持的授权类型: unknown_grant_type
WARN  客户端ID已存在: my-app
```

### 错误日志

```
ERROR 新增客户端失败: 客户端ID不能为空
ERROR 解析 clientSettings 失败: Unexpected character...
ERROR 解析 tokenSettings 失败: Invalid JSON...
```

## 测试示例

### 单元测试

```java
@Test
public void testInsertClient() {
    Oauth2RegisteredClient client = new Oauth2RegisteredClient();
    client.setClientId("test-app");
    client.setClientName("测试应用");
    client.setClientSecret("test-secret");
    client.setAuthorizationGrantTypes("authorization_code,refresh_token");
    client.setRedirectUris("http://localhost:8080/callback");
    client.setScopes("user.read");

    int result = oauth2RegisteredClientService.insertOauth2RegisteredClient(client);
    assertEquals(1, result);
}
```

### 验证客户端

```java
RegisteredClient registeredClient = jdbcRegisteredClientRepository.findByClientId("test-app");
assertNotNull(registeredClient);
assertEquals("测试应用", registeredClient.getClientName());
assertTrue(registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE));
```

## 与前端集成

### API 请求示例

```json
POST /api/oauth2/client

{
  "clientId": "my-app",
  "clientName": "我的应用",
  "clientSecret": "my-secret",
  "authorizationGrantTypes": "authorization_code,refresh_token",
  "redirectUris": "http://localhost:8080/callback",
  "scopes": "user.read,user.write",
  "clientSettings": "{\"requireAuthorizationConsent\": true}",
  "tokenSettings": "{\"accessTokenTimeToLive\": 3600}"
}
```

## 最佳实践

1. **密钥安全**
   - 生产环境使用强密钥
   - 定期轮换密钥
   - 不在日志中输出密钥

2. **授权类型选择**
   - Web应用：authorization_code + PKCE
   - 移动应用：authorization_code + PKCE（无密钥）
   - 微服务：client_credentials
   - 内部应用：password（仅限内部）

3. **Token 有效期**
   - Access Token：短期（15分钟-2小时）
   - Refresh Token：长期（7天-30天）
   - 根据安全需求调整

4. **Scope 设计**
   - 细粒度划分
   - 最小权限原则
   - 清晰命名

## 相关文档

- `ClientInitializer.java` - 客户端初始化示例
- `OAuth2客户端管理功能实现文档.md` - 完整功能文档
- `登录超时配置说明.md` - Token 超时配置
