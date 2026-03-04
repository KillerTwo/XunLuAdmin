# XunLu OAuth2统一认证平台 - 完整实施指南

## 📋 目录

1. [架构概述](#架构概述)
2. [已完成的安全修复](#已完成的安全修复)
3. [后端实施步骤](#后端实施步骤)
4. [前端实施步骤](#前端实施步骤)
5. [第三方应用接入指南](#第三方应用接入指南)
6. [微服务模版使用指南](#微服务模版使用指南)
7. [部署检查清单](#部署检查清单)

---

## 架构概述

### 双定位设计

#### 定位1: 统一认证中心（OAuth2 Provider）
- 类似GitHub OAuth Apps功能
- 第三方应用可注册客户端凭证
- 使用标准OAuth2授权码模式
- 支持OIDC协议

#### 定位2: 微服务开发模版
- 开箱即用的用户管理系统
- 内置管理后台认证
- 微服务间安全调用
- 网关统一鉴权

### 客户端类型

| 客户端类型 | Client ID | 授权模式 | 使用场景 |
|-----------|-----------|---------|---------|
| 内部管理后台 | xunlu-admin-web | Password | 本系统React前端 |
| 第三方应用 | 动态注册 | Authorization Code + PKCE | 外部应用接入 |
| 微服务 | microservice-internal | Client Credentials | 服务间调用 |

---

## 已完成的安全修复

### ✅ 修复1: 移除前端硬编码的客户端凭证

**问题**: `frontend-cloud/src/services/system/login.ts`中硬编码了`client_secret`

**修复**:
```typescript
// 修改前（不安全）
'Authorization': 'Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ='

// 修改后（安全）
formData.append('client_id', 'xunlu-admin-web');  // 公共客户端，无需secret
// 移除Authorization header
```

**影响**: 前端代码不再暴露任何敏感凭证

---

### ✅ 修复2: Cookie密码存储

**问题**: `frontend-cloud/src/pages/user/Login/index.tsx`中明文存储密码

**修复**:
```typescript
// 修改前（不安全）
Cookies.set('password', values.password || '', { expires: 30 });

// 修改后（安全）
// 完全移除密码存储逻辑
// 仅保存用户名，添加secure和sameSite标志
Cookies.set('username', values.username || '', {
  expires: 30,
  secure: window.location.protocol === 'https:',
  sameSite: 'strict'
});
```

**影响**: 用户密码不再存储在客户端

---

### ✅ 修复3: 客户端配置优化

**问题**: `AuthorizationServerConfig.java`中硬编码客户端配置

**修复**:
- 创建`ClientInitializer.java`自动初始化默认客户端
- 移除硬编码配置
- 支持通过管理界面动态注册客户端

**影响**: 客户端配置更灵活，支持动态管理

---

## 后端实施步骤

### 步骤1: 数据库准备

确保已执行OAuth2相关表的建表脚本：

```sql
-- oauth2_registered_client 表
-- oauth2_authorization 表
-- oauth2_authorization_consent 表

-- 位置: XunLuAdminCloud/oauth2-server/src/main/resources/sql/
```

### 步骤2: JWT密钥持久化配置

**创建文件**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/JwkConfiguration.java`

```java
package org.wm.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wm.jose.Jwks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * JWT密钥配置
 * 解决服务重启后token失效问题
 */
@Slf4j
@Configuration
public class JwkConfiguration {

    @Value("${oauth2.jwk.keystore-path:config/oauth2-keystore.jks}")
    private String keystorePath;

    @Value("${oauth2.jwk.keystore-password:change-this-password}")
    private String keystorePassword;

    @Value("${oauth2.jwk.key-alias:oauth2-key}")
    private String keyAlias;

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        try {
            File keystoreFile = new File(keystorePath);
            RSAKey rsaKey;

            if (keystoreFile.exists()) {
                // 从文件加载已有密钥
                log.info("从文件加载JWT密钥: {}", keystorePath);
                KeyStore keyStore = KeyStore.getInstance("JKS");
                try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                    keyStore.load(fis, keystorePassword.toCharArray());
                }
                rsaKey = RSAKey.load(keyStore, keyAlias, keystorePassword.toCharArray());
            } else {
                // 首次启动，生成并保存密钥
                log.warn("JWT密钥文件不存在，生成新密钥并保存到: {}", keystorePath);
                rsaKey = Jwks.generateRsa();

                // 确保目录存在
                Files.createDirectories(Paths.get(keystorePath).getParent());

                // 保存密钥到文件
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(null, null);
                keyStore.setKeyEntry(
                    keyAlias,
                    rsaKey.toPrivateKey(),
                    keystorePassword.toCharArray(),
                    new java.security.cert.Certificate[]{
                        // 注意：这里需要生成自签名证书
                        // 生产环境应使用正式证书
                    }
                );

                try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                    keyStore.store(fos, keystorePassword.toCharArray());
                }

                log.info("JWT密钥已保存");
            }

            JWKSet jwkSet = new JWKSet(rsaKey);
            return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);

        } catch (Exception e) {
            log.error("JWT密钥配置失败", e);
            throw new IllegalStateException("Failed to configure JWK", e);
        }
    }
}
```

**配置文件**: `XunLuAdminCloud/oauth2-server/src/main/resources/application.yml`

```yaml
oauth2:
  jwk:
    keystore-path: config/oauth2-keystore.jks
    keystore-password: ${JWT_KEYSTORE_PASSWORD:change-this-password-in-production}
    key-alias: oauth2-key
```

**修改**: `AuthorizationServerConfig.java` - 移除现有的`jwkSource()` Bean定义（由JwkConfiguration提供）

---

### 步骤3: CSRF保护启用

**创建文件**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/CsrfConfiguration.java`

```java
package org.wm.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * SPA应用的CSRF处理器
 * 支持Cookie和Header两种方式传递CSRF token
 */
public final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {

    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       Supplier<CsrfToken> csrfToken) {
        // 将CSRF token同时写入Cookie和Response attribute
        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        // 优先从Header读取，其次从参数读取
        if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
            return super.resolveCsrfTokenValue(request, csrfToken);
        }
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}
```

**修改**: `AuthorizationServerConfig.java` - 启用CSRF

```java
// 在authorizationServerSecurityFilterChain方法中
http.securityMatcher(endpointsMatcher)
    .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests.anyRequest().authenticated()
    )
    // 修改CSRF配置
    .csrf(csrf -> csrf
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
    )
    .apply(validateCodeConfigurer).and()
    .apply(authorizationServerConfigurer);
```

---

### 步骤4: Token安全存储方案

**创建文件**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/controller/TokenController.java`

```java
package org.wm.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Token安全处理控制器
 * 将refresh_token设置为HttpOnly Cookie
 */
@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class TokenController {

    /**
     * Token响应增强
     * 将refresh_token通过HttpOnly Cookie返回，增强安全性
     */
    @PostMapping("/token/secure")
    public ResponseEntity<?> secureTokenResponse(
            @RequestBody OAuth2AccessTokenResponse tokenResponse,
            HttpServletResponse response) {

        // 如果存在refresh_token，设置为HttpOnly Cookie
        if (tokenResponse.getRefreshToken() != null) {
            Cookie refreshTokenCookie = new Cookie("refresh_token",
                tokenResponse.getRefreshToken().getTokenValue());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);  // HTTPS下启用
            refreshTokenCookie.setPath("/api/oauth2");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);  // 7天
            response.addCookie(refreshTokenCookie);
        }

        // 返回响应时不包含refresh_token（已在Cookie中）
        return ResponseEntity.ok(Map.of(
            "access_token", tokenResponse.getAccessToken().getTokenValue(),
            "token_type", "Bearer",
            "expires_in", tokenResponse.getAccessToken().getExpiresIn().getSeconds(),
            "scope", String.join(" ", tokenResponse.getAccessToken().getScopes())
        ));
    }
}
```

---

### 步骤5: Redis Authorization Service完善

**修改**: `RedisOAuth2AuthorizationService.java`

```java
@Override
@Nullable
public OAuth2Authorization findById(String id) {
    Assert.hasText(id, "id cannot be empty");

    // 方案1: 使用额外的id映射（推荐）
    String idKey = buildKey("id", id);
    redisTemplate.setValueSerializer(RedisSerializer.java());
    return (OAuth2Authorization) redisTemplate.opsForValue().get(idKey);
}

// 修改save方法，额外存储id映射
@Override
public void save(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");

    // 原有逻辑保持不变...

    // 新增: 保存id映射
    String idKey = buildKey("id", authorization.getId());
    redisTemplate.setValueSerializer(RedisSerializer.java());
    redisTemplate.opsForValue().set(idKey, authorization, TIMEOUT, TimeUnit.MINUTES);
}
```

---

## 前端实施步骤

### 步骤1: Token管理工具类

**创建文件**: `frontend-cloud/src/utils/tokenManager.ts`

```typescript
/**
 * Token安全管理工具
 * Access Token存储在sessionStorage（内存）
 * Refresh Token通过HttpOnly Cookie管理（后端设置）
 */

const ACCESS_TOKEN_KEY = 'xunlu_access_token';
const TOKEN_EXPIRY_KEY = 'xunlu_token_expiry';

export class TokenManager {

  /**
   * 设置access token
   */
  static setAccessToken(token: string, expiresIn: number): void {
    sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
    // 计算过期时间（提前5分钟刷新）
    const expiryTime = Date.now() + (expiresIn - 300) * 1000;
    sessionStorage.setItem(TOKEN_EXPIRY_KEY, expiryTime.toString());
  }

  /**
   * 获取access token
   */
  static getAccessToken(): string | null {
    const token = sessionStorage.getItem(ACCESS_TOKEN_KEY);
    if (!token) return null;

    // 检查是否过期
    if (this.isTokenExpired()) {
      this.clearTokens();
      return null;
    }

    return token;
  }

  /**
   * 检查token是否即将过期
   */
  static isTokenExpired(): boolean {
    const expiryTime = sessionStorage.getItem(TOKEN_EXPIRY_KEY);
    if (!expiryTime) return true;
    return Date.now() >= parseInt(expiryTime);
  }

  /**
   * 清除所有token
   */
  static clearTokens(): void {
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);
    sessionStorage.removeItem(TOKEN_EXPIRY_KEY);
    // refresh_token在HttpOnly Cookie中，通过后端清除
  }

  /**
   * 刷新access token
   */
  static async refreshAccessToken(): Promise<string | null> {
    try {
      const response = await fetch('/api/oauth2/token', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          grant_type: 'refresh_token',
          client_id: 'xunlu-admin-web',
        }),
        credentials: 'include',  // 发送HttpOnly Cookie
      });

      if (!response.ok) {
        throw new Error('Token refresh failed');
      }

      const data = await response.json();
      if (data.access_token) {
        this.setAccessToken(data.access_token, data.expires_in || 7200);
        return data.access_token;
      }

      return null;
    } catch (error) {
      console.error('Token refresh error:', error);
      this.clearTokens();
      return null;
    }
  }
}
```

---

### 步骤2: 更新登录逻辑

**修改**: `frontend-cloud/src/pages/user/Login/index.tsx`

```typescript
import { TokenManager } from '@/utils/tokenManager';

const handleSubmit = async (values: SYSTEM.LoginParams) => {
  // ... 现有验证逻辑 ...

  const msg = await login({ ...values, code: captcha, uuid: codeUuid, type: type, username: username });

  if (msg?.access_token !== null) {
    // 使用TokenManager安全存储token
    TokenManager.setAccessToken(msg.access_token, msg.expires_in || 7200);

    notification.success({ message: '登录成功'});
    await fetchUserInfo();
    await fetchMenuInfo();

    const { query } = history.location;
    const { redirect } = query as { redirect: string };
    history.push(redirect || '/');
    return;
  }
};
```

---

### 步骤3: 请求拦截器增强

**修改**: `frontend-cloud/src/app.tsx`

```typescript
import { TokenManager } from '@/utils/tokenManager';

const authHeaderInterceptor = (url: string, options: RequestOptionsInit) => {
  if (url.indexOf('/login/') !== -1 || url.indexOf('/logout') !== -1 || url.indexOf("token") !== -1) {
    return {
      url: `${url}`,
      options: { ...options, interceptors: true },
    };
  } else {
    const token = TokenManager.getAccessToken();
    let authHeader = {};

    if (token) {
      authHeader = { Authorization: `Bearer ${token}` };
    }

    return {
      url: `${url}`,
      options: {
        ...options,
        interceptors: true,
        headers: { ...options.headers, ...authHeader },
        credentials: 'include'  // 发送Cookie
      },
    };
  }
};

const demoResponseInterceptors = async (response: Response, options: RequestOptionsInit) => {
  // 401错误处理
  if (response.status === 401 && !options.url?.includes('/oauth2/token')) {
    // 尝试刷新token
    const newToken = await TokenManager.refreshAccessToken();

    if (newToken) {
      // 重试原请求
      const retryOptions = {
        ...options,
        headers: {
          ...options.headers,
          Authorization: `Bearer ${newToken}`,
        },
      };
      return fetch(response.url, retryOptions);
    } else {
      // 刷新失败，跳转登录
      TokenManager.clearTokens();
      history.push('/user/login');
    }
  }

  return response;
};
```

---

## 第三方应用接入指南

### 场景: 外部Web应用使用XunLu进行用户认证

#### 步骤1: 注册客户端

通过XunLu管理后台注册应用：

```bash
POST /api/oauth2/clients/register

{
  "clientName": "我的应用",
  "redirectUris": ["https://myapp.com/callback"],
  "scopes": ["openid", "profile", "email"],
  "clientType": "third_party_app"
}

# 响应
{
  "clientId": "myapp-xxxxx",
  "clientSecret": "secret-xxxxx",  # 请妥善保管
  "message": "请妥善保管client_secret，系统不会再次显示完整密钥"
}
```

#### 步骤2: 实现OAuth2授权码流程

**第三方应用后端代码示例（Spring Boot）**:

```java
@Configuration
@EnableWebSecurity
public class OAuth2ClientConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                    .baseUri("/oauth2/authorization")
                )
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*")
                )
            );
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(xunluClientRegistration());
    }

    private ClientRegistration xunluClientRegistration() {
        return ClientRegistration.withRegistrationId("xunlu")
                .clientId("myapp-xxxxx")  // 从XunLu获取
                .clientSecret("secret-xxxxx")  // 从XunLu获取
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://auth.xunlu.com/oauth2/authorize")
                .tokenUri("https://auth.xunlu.com/oauth2/token")
                .userInfoUri("https://auth.xunlu.com/oauth2/userinfo")
                .userNameAttributeName("sub")
                .jwkSetUri("https://auth.xunlu.com/oauth2/jwks")
                .clientName("XunLu")
                .build();
    }
}
```

**application.yml配置**:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          xunlu:
            client-id: myapp-xxxxx
            client-secret: secret-xxxxx
            scope:
              - openid
              - profile
              - email
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          xunlu:
            authorization-uri: https://auth.xunlu.com/oauth2/authorize
            token-uri: https://auth.xunlu.com/oauth2/token
            user-info-uri: https://auth.xunlu.com/oauth2/userinfo
            user-name-attribute: sub
            jwk-set-uri: https://auth.xunlu.com/oauth2/jwks
```

#### 步骤3: 前端登录按钮

```html
<a href="/oauth2/authorization/xunlu">
  使用XunLu登录
</a>
```

---

## 微服务模版使用指南

### 场景: 基于XunLu开发新的业务系统

#### 步骤1: 克隆项目

```bash
git clone https://github.com/your-org/XunLuAdmin.git my-business-system
cd my-business-system
```

#### 步骤2: 定制配置

**修改项目名称**: `pom.xml`

```xml
<groupId>com.mycompany</groupId>
<artifactId>my-business-system</artifactId>
<name>My Business System</name>
```

**修改数据库配置**: `application-prod.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/my_business_db?useSSL=false
    username: myuser
    password: ${DB_PASSWORD}
```

#### 步骤3: 添加业务模块

```bash
cd XunLuAdminCloud/services
mkdir my-service
# 创建新的业务服务
```

**新服务结构**:

```
my-service/
├── src/main/java/com/mycompany/myservice/
│   ├── controller/
│   ├── service/
│   ├── mapper/
│   └── MyServiceApplication.java
├── src/main/resources/
│   ├── application.yml
│   └── mapper/
└── pom.xml
```

#### 步骤4: 集成认证

新服务自动继承认证功能，无需额外配置：

```java
@RestController
@RequestMapping("/api/my-business")
public class MyBusinessController {

    // 自动获取当前登录用户
    @GetMapping("/userInfo")
    public ResponseEntity<?> getCurrentUser() {
        SecurityContextUser user = SecurityUtils.getLoginUser();
        return ResponseEntity.ok(user);
    }

    // 权限控制
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        // 业务逻辑
    }
}
```

---

## 部署检查清单

### 安全检查

- [ ] 修改所有默认密码
  - [ ] JWT keystore密码
  - [ ] 数据库密码
  - [ ] Redis密码
  - [ ] 默认客户端secret

- [ ] HTTPS配置
  - [ ] 配置SSL证书
  - [ ] 强制HTTPS重定向
  - [ ] 设置Cookie的secure标志

- [ ] CSRF保护
  - [ ] 确认CSRF已启用
  - [ ] 前端正确处理CSRF token

- [ ] 环境变量
  - [ ] 敏感配置使用环境变量
  - [ ] 不在代码中硬编码密码

### 功能检查

- [ ] OAuth2端点测试
  - [ ] `/oauth2/authorize` - 授权端点
  - [ ] `/oauth2/token` - Token端点
  - [ ] `/oauth2/userinfo` - 用户信息端点
  - [ ] `/oauth2/jwks` - JWK Set端点

- [ ] 客户端配置
  - [ ] 内部管理后台客户端正常
  - [ ] 第三方应用注册功能正常
  - [ ] 微服务客户端正常

- [ ] Token流程
  - [ ] Access Token正确颁发
  - [ ] Refresh Token正确刷新
  - [ ] Token过期后自动刷新

### 性能检查

- [ ] Redis连接池配置
- [ ] 数据库连接池配置
- [ ] JWT签名性能（考虑使用ECDSA替代RSA）
- [ ] Token有效期合理设置

---

## 下一步工作

1. **创建客户端管理UI** - 在管理后台添加OAuth2客户端管理页面
2. **添加审计日志** - 记录所有OAuth2授权操作
3. **实现用户授权同意页面** - 美化`/oauth2/consent`页面
4. **添加客户端图标和描述** - 在授权页面展示客户端信息
5. **实现Scope细粒度控制** - 根据业务需求定义详细的权限范围
6. **性能监控** - 添加OAuth2相关的metrics

---

## 常见问题FAQ

### Q1: 服务重启后token全部失效怎么办？
A: 确保JWT密钥持久化配置正确，密钥文件应保存在持久化存储中（非容器临时目录）

### Q2: 如何支持多实例部署？
A:
- JWT密钥使用共享存储（NFS/对象存储）
- 或使用数据库存储JWK
- Redis使用集群模式

### Q3: 第三方应用如何处理用户注销？
A: 实现OIDC的RP-Initiated Logout或Back-Channel Logout

### Q4: 如何限制客户端的请求频率？
A: 在网关层添加限流策略，针对client_id进行限流

---

## 联系支持

- 问题反馈: [GitHub Issues](https://github.com/your-org/XunLuAdmin/issues)
- 文档: [在线文档](https://docs.xunlu.com)
- 邮件: support@xunlu.com
