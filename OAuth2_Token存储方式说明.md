# OAuth2 Token 存储方式说明

## 概述

本文档说明了后端 OAuth2 Token 的存储方式、实现细节和优缺点分析。

## Token 存储方式

### ✅ 当前使用：Redis 存储

后端使用 **Redis** 作为 OAuth2 Token 的存储介质，通过自定义的 `RedisOAuth2AuthorizationService` 实现。

## 实现细节

### 1. 存储服务实现

**文件位置**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/authorization/RedisOAuth2AuthorizationService.java`

**类定义**:
```java
@Slf4j
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final RegisteredClientRepository registeredClientRepository;
}
```

### 2. 存储的 Token 类型

Redis 中存储了多种类型的 Token：

1. **Access Token** (访问令牌)
2. **Refresh Token** (刷新令牌)
3. **Authorization Code** (授权码)
4. **State** (状态参数)

### 3. Redis Key 命名规则

```java
private String buildKey(String type, String id) {
    return String.format("%s::%s::%s", AUTHORIZATION, type, id);
}
```

**Key 格式**: `token::{token类型}::{token值}`

**示例**:
- Access Token: `token::access_token::eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...`
- Refresh Token: `token::refresh_token::GJdwL0YvN9xXhWQzO1aNbHRhM...`
- Authorization Code: `token::code::ABC123...`
- State: `token::state::XYZ789...`

### 4. 过期时间策略

#### Access Token
```java
if (isAccessToken(authorization)) {
    OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
    long between = ChronoUnit.SECONDS.between(
        Objects.requireNonNull(accessToken.getIssuedAt()),
        accessToken.getExpiresAt()
    );
    redisTemplate.opsForValue().set(
        buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()),
        authorization,
        between,
        TimeUnit.SECONDS
    );
}
```

**过期时间**: 根据客户端配置的 `accessTokenTimeToLive` 自动设置
- 对于 `xunlu-admin-web`: 2 小时（7200 秒）

#### Refresh Token
```java
if (isRefreshToken(authorization)) {
    OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
    long between = ChronoUnit.SECONDS.between(
        Objects.requireNonNull(refreshToken.getIssuedAt()),
        refreshToken.getExpiresAt()
    );
    redisTemplate.opsForValue().set(
        buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()),
        authorization,
        between,
        TimeUnit.SECONDS
    );
}
```

**过期时间**: 根据客户端配置的 `refreshTokenTimeToLive` 自动设置
- 对于 `xunlu-admin-web`: 7 天（604800 秒）

#### Authorization Code
```java
if (isCode(authorization)) {
    OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
    long between = ChronoUnit.MINUTES.between(
        Objects.requireNonNull(authorizationCodeToken.getIssuedAt()),
        authorizationCodeToken.getExpiresAt()
    );
    redisTemplate.opsForValue().set(
        buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()),
        authorization,
        between,
        TimeUnit.MINUTES
    );
}
```

**过期时间**: 通常 5-10 分钟（OAuth2 标准建议）

#### State
```java
if (isState(authorization)) {
    String token = authorization.getAttribute("state");
    redisTemplate.opsForValue().set(
        buildKey(OAuth2ParameterNames.STATE, token),
        authorization,
        TIMEOUT,  // 10 分钟
        TimeUnit.MINUTES
    );
}
```

**过期时间**: 固定 10 分钟

### 5. 配置注册

**文件位置**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/AuthorizationServerConfig.java:312-314`

```java
@Bean
public OAuth2AuthorizationService auth2AuthorizationService() {
    return new RedisOAuth2AuthorizationService(redisTemplate, registeredClientRepository());
}
```

## 存储数据结构

### Redis 中存储的完整授权信息

每个 Token 对应的 Value 是一个完整的 `OAuth2Authorization` 对象，包含：

```json
{
  "id": "授权ID",
  "registeredClientId": "客户端ID",
  "principalName": "用户名",
  "authorizationGrantType": "授权类型",
  "attributes": {
    // 自定义属性
  },
  "tokens": {
    "access_token": {
      "token": {
        "tokenValue": "访问令牌值",
        "issuedAt": "颁发时间",
        "expiresAt": "过期时间",
        "tokenType": "Bearer"
      }
    },
    "refresh_token": {
      "token": {
        "tokenValue": "刷新令牌值",
        "issuedAt": "颁发时间",
        "expiresAt": "过期时间"
      }
    }
  }
}
```

## 与数据库的关系

### 数据库存储内容

系统中还存在数据库表 `oauth2_authorization`（见 SQL 文件），但**当前未启用**：

**文件位置**: `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/AuthorizationServerConfig.java:317-354`

```java
// @Bean  // 已注释掉，未启用
public OAuth2AuthorizationService authorizationService() {
    var service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository());
    // ... JDBC 配置
    return service;
}
```

### 数据库中存储的其他内容

虽然 Token 不存储在数据库中，但以下内容仍存储在数据库：

1. **OAuth2 客户端配置** (`oauth2_registered_client` 表)
   - 客户端 ID、Secret
   - 授权类型
   - Token 有效期配置
   - Redirect URIs
   - Scopes

2. **用户授权记录** (`oauth2_authorization_consent` 表)
   - 用户对第三方应用的授权同意记录

## Redis vs 数据库存储对比

### Redis 存储（当前方案）

#### ✅ 优点

1. **高性能**:
   - 内存读写，速度快（微秒级）
   - 适合高并发场景
   - Token 验证频繁，Redis 更适合

2. **自动过期**:
   - Redis 原生支持 TTL（Time To Live）
   - Token 过期后自动删除，无需额外清理任务
   - 节省存储空间

3. **减轻数据库压力**:
   - Token 生命周期短，频繁读写
   - 使用 Redis 可减轻数据库 I/O 压力

4. **简化运维**:
   - 无需定期清理过期 Token
   - 无需索引优化
   - 存储和查询逻辑简单

#### ⚠️ 缺点

1. **持久化风险**:
   - Redis 重启可能导致 Token 丢失（取决于持久化配置）
   - 需要配置 RDB 或 AOF 持久化

2. **资源限制**:
   - 受限于 Redis 内存大小
   - 大量 Token 可能占用较多内存

3. **无法审计**:
   - Token 过期后自动删除
   - 无法追溯历史 Token 使用记录

4. **分布式复杂性**:
   - 多实例部署需要 Redis 集群
   - 需要考虑主从同步延迟

### 数据库存储

#### ✅ 优点

1. **持久化保证**:
   - 数据永久保存
   - 不会因重启丢失

2. **审计能力**:
   - 可保留历史 Token 记录
   - 便于审计和分析

3. **无内存限制**:
   - 可存储大量历史数据

4. **ACID 保证**:
   - 事务一致性
   - 数据可靠性高

#### ⚠️ 缺点

1. **性能较低**:
   - 磁盘 I/O 慢于内存
   - 高并发场景可能成为瓶颈

2. **需要定期清理**:
   - 过期 Token 需要定时任务清理
   - 清理任务可能影响性能

3. **存储空间增长**:
   - 历史数据积累
   - 需要定期归档

4. **查询复杂**:
   - 需要建立索引
   - 多表关联查询

## 混合存储方案

### 推荐架构（生产环境）

```
┌─────────────────────────────────────────┐
│         Token 生命周期管理              │
├─────────────────────────────────────────┤
│                                         │
│  颁发 Token                             │
│    ↓                                    │
│  存储到 Redis (有效期内)                │
│    ↓                                    │
│  Token 验证 (从 Redis 读取)             │
│    ↓                                    │
│  Token 过期 (Redis 自动删除)            │
│    ↓ (可选)                             │
│  重要 Token 记录到数据库 (审计用)       │
│                                         │
└─────────────────────────────────────────┘
```

**实现建议**:

1. **活跃 Token**: 存储在 Redis
   - Access Token
   - Refresh Token
   - Authorization Code

2. **审计记录**: 存储在数据库
   - Token 颁发记录（可选）
   - 重要操作关联的 Token（可选）
   - 异常 Token 记录

3. **异步写入**:
   - 颁发 Token 时同步写 Redis
   - 异步记录到数据库（不阻塞主流程）

## Redis 持久化配置建议

为了保证 Token 数据安全，建议配置 Redis 持久化：

### RDB（快照）

```conf
# 900秒（15分钟）内至少1个key发生变化，则dump快照
save 900 1
save 300 10
save 60 10000
```

### AOF（追加日志）

```conf
# 开启AOF
appendonly yes
# 每秒同步
appendfsync everysec
```

### 推荐配置

```conf
# 同时开启 RDB 和 AOF
appendonly yes
appendfsync everysec
save 900 1
save 300 10
save 60 10000
```

**权衡**:
- RDB: 恢复速度快，但可能丢失最后一次快照后的数据
- AOF: 数据丢失少，但恢复速度慢
- 推荐两者同时使用（Redis 4.0+ 支持混合持久化）

## Token 丢失的影响和处理

### 场景：Redis 重启导致 Token 丢失

#### 影响

1. **Access Token 丢失**:
   - 用户需要重新登录
   - 或使用 Refresh Token 刷新（如果 Refresh Token 也在 Redis 中，则一并丢失）

2. **Refresh Token 丢失**:
   - 自动登录失效
   - 用户需要重新输入密码登录

#### 解决方案

**方案 1: 自动降级到密码登录**
```
前端检测到 Token 无效
  ↓
尝试使用 Refresh Token 刷新
  ↓
刷新失败（Token 丢失）
  ↓
清除本地存储，跳转到登录页
  ↓
用户重新登录
```

✅ **当前实现已支持此方案**（见 `自动登录功能实现文档.md`）

**方案 2: 配置 Redis 持久化**
- 启用 RDB + AOF
- 最小化数据丢失

**方案 3: Redis 集群 + 主从复制**
- 高可用架构
- 避免单点故障

## 监控建议

### Redis 监控指标

1. **内存使用率**: 应 < 80%
2. **Key 数量**: 监控 Token 数量
3. **过期 Key 数量**: 监控自动清理是否正常
4. **命中率**: 应 > 95%
5. **主从同步延迟**: 应 < 1秒

### 应用监控

1. **Token 颁发速率**: QPS
2. **Token 验证失败率**: 应 < 5%
3. **Refresh Token 使用率**: 评估自动登录效果
4. **Redis 连接异常**: 及时告警

## 总结

| 特性 | Redis 存储（当前方案） | 数据库存储 |
|------|---------------------|-----------|
| 性能 | ⭐⭐⭐⭐⭐ 极快 | ⭐⭐⭐ 较快 |
| 自动过期 | ⭐⭐⭐⭐⭐ 原生支持 | ⭐⭐ 需要定时任务 |
| 持久化 | ⭐⭐⭐ 可配置 | ⭐⭐⭐⭐⭐ 天然持久化 |
| 审计能力 | ⭐ 无历史记录 | ⭐⭐⭐⭐⭐ 完整记录 |
| 运维成本 | ⭐⭐⭐⭐ 简单 | ⭐⭐ 需要清理和优化 |
| 适用场景 | 短期、高频访问 | 长期、审计要求高 |

### 当前方案评估

✅ **优点**:
- 性能优秀，适合高并发
- 自动过期，无需清理
- 实现简单，易于维护

⚠️ **注意事项**:
- 需要配置 Redis 持久化
- 需要监控 Redis 健康状态
- 需要考虑 Redis 故障恢复方案

### 改进建议

1. **生产环境**:
   - 配置 Redis 持久化（RDB + AOF）
   - 使用 Redis 哨兵或集群模式
   - 添加 Redis 监控和告警

2. **审计需求**:
   - 可选择性记录重要 Token 到数据库
   - 异步记录，不影响性能

3. **灾难恢复**:
   - 定期备份 Redis 数据
   - 准备降级方案（切换到数据库存储）

## 相关文件

- `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/authorization/RedisOAuth2AuthorizationService.java` - Redis 存储实现
- `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/AuthorizationServerConfig.java` - 配置注册
- `XunLuAdminCloud/oauth2-server/src/main/resources/sql/oauth2-authorization-schema.sql` - 数据库表结构（备用）
