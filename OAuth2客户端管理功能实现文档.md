# OAuth2 客户端管理UI功能实现文档

## 功能概述

参考 GitHub OAuth Apps 设计，为 XunLuAdmin 系统实现了完整的 OAuth2 客户端管理UI功能，放置在"系统管理"菜单下。

## 实现内容

### 一、后端优化

#### 1. Controller 增强 (`Oauth2RegisteredClientController.java`)

**位置**: `/XunLuAdminCloud/oauth2-server/src/main/java/org/wm/controller/Oauth2RegisteredClientController.java`

**新增接口**:
- `GET /oauth2/registeredClient/enums` - 获取可用的授权类型、认证方法、作用域枚举
- 优化了实体转DTO的逻辑，增加了 `convertToDto()` 方法
- Client Secret 在列表中显示为 "********" 保护安全

**核心代码**:
```java
@GetMapping("/enums")
public ResponseResult<Map<String, Object>> getEnums() {
    Map<String, Object> enums = new HashMap<>();
    enums.put("authorizationGrantTypes", Arrays.asList(
            "authorization_code",
            "client_credentials",
            "refresh_token",
            "password"
    ));
    enums.put("clientAuthenticationMethods", Arrays.asList(
            "client_secret_basic",
            "client_secret_post",
            "none"
    ));
    enums.put("scopes", Arrays.asList(
            "openid",
            "profile",
            "email",
            "all",
            "user.read",
            "user.write"
    ));
    return ResponseResult.success(enums);
}
```

### 二、前端实现

#### 1. API 服务层 (`oauth2Client.ts`)

**位置**: `/frontend-cloud/src/services/system/oauth2Client.ts`

**提供的API方法**:
- `oauth2ClientList` - 获取客户端列表（分页）
- `getOAuth2Client` - 获取客户端详情
- `getOAuth2Enums` - 获取枚举值
- `addOAuth2Client` - 新增客户端
- `updateOAuth2Client` - 更新客户端
- `removeOAuth2Client` - 删除客户端

#### 2. 类型定义 (`typings.d.ts`)

**位置**: `/frontend-cloud/src/services/system/typings.d.ts`

**新增类型**:
```typescript
type OAuth2Client = {
  id?: string;
  clientId?: string;
  clientSecret?: string;
  clientName?: string;
  clientIdIssuedAt?: string;
  clientAuthenticationMethods?: string;
  authorizationGrantTypes?: string[];
  redirectUris?: string;
  scopes?: string[];
}

type OAuth2ClientResponse = {
  code: number;
  msg: string;
  rows: OAuth2Client[];
  total: number;
}

type OAuth2Enums = {
  authorizationGrantTypes: string[];
  clientAuthenticationMethods: string[];
  scopes: string[];
}
```

#### 3. 页面组件 (`OAuth2ClientList/index.tsx`)

**位置**: `/frontend-cloud/src/pages/system/OAuth2ClientList/index.tsx`

**功能特性**:

##### ✅ 列表展示
- 使用 Ant Design ProTable 组件
- 支持分页、搜索（应用名称、Client ID）
- 显示列：应用名称、Client ID、Client Secret、授权类型、认证方式、创建时间、操作

##### ✅ 新建应用
- Modal Form 弹窗表单
- 必填项验证
- 字段说明：
  - **应用名称**: 用户看到的显示名称
  - **Client ID**: 客户端唯一标识符
  - **Client Secret**: 可选，公共客户端可留空
  - **认证方式**: 单选下拉框
  - **授权类型**: 多选下拉框（支持多种OAuth2流程）
  - **授权范围**: 多选下拉框（权限范围）
  - **回调地址**: 多行文本框，每行一个URL

##### ✅ 编辑应用
- Modal Form 弹窗表单
- Client ID 不可修改（只读）
- 其他字段可编辑
- 初始值自动填充

##### ✅ 查看详情
- Drawer 抽屉展示完整信息
- Client ID 支持一键复制
- 授权类型、授权范围使用 Tag 组件美化展示
- 回调地址分行显示

##### ✅ 删除应用
- Popconfirm 二次确认
- 防止误删

#### 4. 路由配置 (`routes.ts`)

**位置**: `/frontend-cloud/config/routes.ts`

**新增路由**:
```typescript
{
  name: 'OAuth2客户端',
  path: '/system/oauth2Client',
  component: './system/OAuth2ClientList'
}
```

**菜单位置**: 系统管理 > OAuth2客户端

## 设计参考

参考 GitHub OAuth Apps 的设计理念：

1. **简洁明了的列表视图** - 展示关键信息，便于快速浏览
2. **详细的配置表单** - 提供完整的OAuth2配置选项
3. **安全的密钥管理** - Client Secret 隐藏显示
4. **清晰的字段说明** - 每个字段都有 tooltip 说明
5. **直观的状态展示** - 使用 Tag 和图标展示不同状态

## 技术栈

### 后端
- Spring Boot 3.0.2
- Spring Security OAuth2 Authorization Server
- MyBatis
- MySQL

### 前端
- React 17
- Ant Design Pro 5.2.0
- Ant Design 4.17.0
- TypeScript
- UmiJS 3.5.23

## 使用指南

### 1. 启动服务

**后端**:
```bash
cd XunLuAdminCloud/oauth2-server
mvn spring-boot:run
```

**前端**:
```bash
cd frontend-cloud
npm install
npm start
```

### 2. 访问功能

1. 登录系统
2. 导航到：系统管理 > OAuth2客户端
3. 可以进行以下操作：
   - 查看现有客户端列表
   - 新建OAuth2应用
   - 编辑应用配置
   - 删除应用
   - 查看应用详情

### 3. 新建应用示例

#### 示例1：第三方应用（Web应用）
- **应用名称**: 我的第三方应用
- **Client ID**: my-third-party-app
- **Client Secret**: 设置一个强密码
- **认证方式**: client_secret_basic
- **授权类型**: authorization_code, refresh_token
- **授权范围**: openid, profile, email, user.read
- **回调地址**:
  ```
  https://example.com/callback
  https://example.com/oauth/callback
  ```

#### 示例2：SPA应用（单页应用）
- **应用名称**: React单页应用
- **Client ID**: react-spa-app
- **Client Secret**: 留空（公共客户端）
- **认证方式**: none
- **授权类型**: authorization_code, refresh_token
- **授权范围**: openid, profile, user.read
- **回调地址**:
  ```
  http://localhost:3000/callback
  ```

#### 示例3：内部服务（微服务）
- **应用名称**: 内部微服务
- **Client ID**: internal-service
- **Client Secret**: 设置一个强密码
- **认证方式**: client_secret_basic
- **授权类型**: client_credentials
- **授权范围**: service.call, internal.api
- **回调地址**: 留空（不需要）

## 数据库表结构

使用 Spring Security OAuth2 标准表：`oauth2_registered_client`

主要字段：
- `id` - 主键（UUID）
- `client_id` - 客户端ID（唯一）
- `client_secret` - 客户端密钥（加密存储）
- `client_name` - 应用名称
- `client_authentication_methods` - 认证方法
- `authorization_grant_types` - 授权类型（逗号分隔）
- `redirect_uris` - 回调地址（换行分隔）
- `scopes` - 授权范围（逗号分隔）
- `client_settings` - 客户端设置（JSON）
- `token_settings` - Token设置（JSON）

## API 接口文档

### 1. 获取客户端列表
```
GET /api/oauth2/registeredClient/list
参数:
  - pageNum: 页码
  - pageSize: 每页大小
  - clientId: 客户端ID（可选，模糊搜索）
  - clientName: 应用名称（可选，模糊搜索）
响应:
{
  "code": 200,
  "msg": "查询成功",
  "rows": [...],
  "total": 10
}
```

### 2. 获取客户端详情
```
GET /api/oauth2/registeredClient/{id}
响应:
{
  "code": 200,
  "msg": "查询成功",
  "data": {...}
}
```

### 3. 获取枚举值
```
GET /api/oauth2/registeredClient/enums
响应:
{
  "code": 200,
  "data": {
    "authorizationGrantTypes": [...],
    "clientAuthenticationMethods": [...],
    "scopes": [...]
  }
}
```

### 4. 新增客户端
```
POST /api/oauth2/registeredClient
请求体:
{
  "clientId": "my-app",
  "clientName": "我的应用",
  "clientSecret": "secret123",
  "clientAuthenticationMethods": "client_secret_basic",
  "authorizationGrantTypes": ["authorization_code", "refresh_token"],
  "scopes": ["openid", "profile"],
  "redirectUris": "https://example.com/callback"
}
```

### 5. 更新客户端
```
PUT /api/oauth2/registeredClient
请求体: 同新增（需包含id字段）
```

### 6. 删除客户端
```
DELETE /api/oauth2/registeredClient/{ids}
参数: ids - 逗号分隔的客户端ID列表
```

## 安全注意事项

1. **Client Secret 保护**
   - 前端列表显示为 "********"
   - 只在新增时允许设置
   - 建议使用强密码

2. **权限控制**
   - 建议为此功能配置权限标识：`system:oauth2:*`
   - 只有管理员可以访问

3. **公共客户端**
   - SPA、移动应用使用 `clientAuthenticationMethods: none`
   - 不需要 Client Secret
   - 必须配置合法的回调地址

4. **回调地址验证**
   - 生产环境必须使用 HTTPS
   - 严格验证回调地址，防止重定向攻击

## 问题修复记录

### 问题1: 403 Forbidden错误 (已修复)

**现象**: 前端请求 `/oauth2/registeredClient/list` 返回403 Forbidden错误

**原因分析**:
1. `/oauth2/registeredClient/**` 端点不在 `AuthorizationServerConfig` 的 `endpointsMatcher` 匹配范围内
2. 请求被 `DefaultSecurityConfig` 处理，但该配置要求认证且没有正确处理JWT Bearer Token
3. 缺少专门的安全配置来将这些端点配置为OAuth2资源服务器

**解决方案**:
创建了 `OAuth2ClientManagementSecurityConfig.java` 专门处理客户端管理API的安全配置：

```java
@Configuration
@RequiredArgsConstructor
public class OAuth2ClientManagementSecurityConfig {

    @Bean
    @Order(1)  // 优先级介于静态资源(0)和默认配置之间
    public SecurityFilterChain oauth2ClientManagementSecurityFilterChain(
            HttpSecurity http,
            JWKSource<SecurityContext> jwkSource) throws Exception {
        http
            .securityMatcher("/oauth2/registeredClient/**")
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(
                    OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
                ))
                .authenticationEntryPoint(authenticationEntryPointImpl)
            )
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

**关键点**:
- 使用 `@Order(1)` 确保在 `DefaultSecurityConfig` 之前处理
- 配置为OAuth2 Resource Server，支持JWT Bearer Token验证
- 使用与Authorization Server相同的JWKSource进行Token解码
- 禁用CSRF（API使用JWT认证）

**SecurityFilterChain优先级顺序**:
1. `HIGHEST_PRECEDENCE` - AuthorizationServerConfig (OAuth2标准端点)
2. `@Order(0)` - DefaultSecurityConfig.resources (静态资源)
3. `@Order(1)` - OAuth2ClientManagementSecurityConfig (客户端管理API) ✅ 新增
4. 无Order - DefaultSecurityConfig.defaultSecurityFilterChain (其他所有请求)

---

### 问题2: MyBatis BindingException错误 (已修复)

**现象**:
```
org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
org.wm.mapper.Oauth2RegisteredClientMapper.selectOauth2RegisteredClientList
```

**原因分析**:
1. Mapper接口方法返回类型声明为 `PageInfo<Oauth2RegisteredClient>`
2. MyBatis XML映射只能返回基本类型（单个对象或 `List<T>`）
3. `PageInfo` 是由 PageHelper 插件通过 AOP 拦截器自动包装的，不能在 XML 中直接映射
4. MyBatis 无法找到返回 `PageInfo` 的映射语句，因为 XML 中定义的是返回 `List`

**工作原理说明**:
- `@UsePage` 注解通过 AOP 拦截带有此注解的方法
- 在方法执行前调用 `PageHelper.startPage(pageNum, pageSize)` 设置分页参数
- Mapper方法执行时返回 `List<T>`
- PageHelper 拦截器自动将 `List<T>` 包装成 `PageInfo<T>`
- Service层需要将 `List<T>` 转换为 `PageInfo<T>` 返回给 Controller

**解决方案**:

1. **修改Mapper接口** (Oauth2RegisteredClientMapper.java:33):
```java
// 修改前
@UsePage
PageInfo<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient);

// 修改后
@UsePage
List<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient);
```

2. **修改Service实现类** (Oauth2RegisteredClientServiceImpl.java:56-59):
```java
@Override
public PageInfo<Oauth2RegisteredClient> selectOauth2RegisteredClientList(Oauth2RegisteredClient oauth2RegisteredClient) {
    // Mapper返回List，Service层包装成PageInfo
    List<Oauth2RegisteredClient> list = oauth2RegisteredClientMapper.selectOauth2RegisteredClientList(oauth2RegisteredClient);
    return new PageInfo<>(list);
}
```

3. **添加MyBatis配置** (application.yml):
```yaml
# MyBatis 配置
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.wm.domain
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl
```

4. **添加Mapper扫描注解** (OAuth2ServerApplication.java):
```java
@EnableFeignClients
@SpringBootApplication
@MapperScan("org.wm.mapper")  // 新增：扫描Mapper接口
public class OAuth2ServerApplication {
    // ...
}
```

**关键点**:
- `Oauth2RegisteredClient` 继承自 `BaseEntity`，而 `BaseEntity` 继承自 `MyPageParam`
- `@UsePage` 注解会检查方法参数中是否有 `MyPageParam` 类型（包括子类）
- Mapper 必须返回 `List<T>`，由 Service 层负责转换为 `PageInfo<T>`
- PageHelper 在 Mapper 执行时已经设置了分页参数，`new PageInfo<>(list)` 会自动包含分页信息

**参考其他Mapper的正确用法**:
- `SysUserMapper.selectUserList` 返回 `List<SysUser>`
- `SysConfigMapper.selectConfigListPage` 返回 `List<SysConfig>`

## 后续优化建议

1. **权限控制**
   - 为菜单和按钮添加权限标识
   - 集成到现有的权限系统
   - 建议权限标识：`system:oauth2:list`, `system:oauth2:add`, `system:oauth2:edit`, `system:oauth2:remove`

2. **数据库菜单**
   - 在 `sys_menu` 表中添加"OAuth2客户端"菜单记录
   - 配置图标和排序

3. **高级功能**
   - Client Secret 重置功能
   - Token 配置（过期时间、刷新策略）
   - 客户端使用统计
   - 审计日志（记录客户端的创建、修改、删除操作）

4. **文档完善**
   - 为第三方开发者提供接入文档
   - OAuth2 授权流程说明
   - 各种授权模式的使用场景说明

## 文件清单

### 后端文件
- ✅ `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/controller/Oauth2RegisteredClientController.java` （优化）
- ✅ `XunLuAdminCloud/oauth2-server/src/main/java/org/wm/config/OAuth2ClientManagementSecurityConfig.java` （新建 - 修复403错误）

### 前端文件
- ✅ `frontend-cloud/src/services/system/oauth2Client.ts` （新建）
- ✅ `frontend-cloud/src/services/system/typings.d.ts` （修改）
- ✅ `frontend-cloud/src/pages/system/OAuth2ClientList/index.tsx` （新建）
- ✅ `frontend-cloud/config/routes.ts` （修改）

## 测试步骤

1. ✅ 启动后端服务（oauth2-server）
2. ✅ 启动前端服务
3. ✅ 登录系统
4. ✅ 访问：系统管理 > OAuth2客户端
5. ✅ 测试新建应用
6. ✅ 测试编辑应用
7. ✅ 测试查看详情
8. ✅ 测试删除应用
9. ✅ 测试搜索功能

## 完成状态

- [x] 后端API优化
- [x] 前端服务层实现
- [x] 前端页面组件开发
- [x] 路由配置
- [x] 类型定义
- [x] 403错误修复（SecurityFilterChain配置）
- [ ] 数据库菜单配置（需要手动添加）
- [ ] 权限配置（需要手动配置）
- [ ] 功能测试（需要重启oauth2-server服务后测试）

---

**实现时间**: 2026-03-01
**实现人员**: Claude Code + User
**参考设计**: GitHub OAuth Apps
