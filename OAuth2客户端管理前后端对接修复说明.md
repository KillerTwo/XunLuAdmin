# OAuth2 客户端管理前后端对接修复说明

## 修复日期
2026-03-02

## 修复概述
修复了前端 OAuth2 客户端管理页面与后端接口的数据格式不匹配问题，确保前后端字段名、字段值格式完全对应。

## 修复的问题

### 1. redirectUris 格式不一致

**问题描述**:
- 前端表单：用户输入换行分隔的 URI（`\n`）
- 后端存储：期望逗号分隔的字符串（`,`）
- 前端显示：需要处理逗号分隔的字符串显示为列表

**修复方案**:

#### 前端提交时转换（index.tsx）
```typescript
// 新增客户端
const processedFields = {
  ...fields,
  redirectUris: fields.redirectUris
    ? fields.redirectUris.split('\n').map(uri => uri.trim()).filter(uri => uri).join(',')
    : ''
};

// 更新客户端
const processedFields = {
  ...currentRow,
  ...fields,
  redirectUris: fields.redirectUris
    ? fields.redirectUris.split('\n').map(uri => uri.trim()).filter(uri => uri).join(',')
    : ''
};
```

#### 前端编辑时回显（index.tsx）
```typescript
initialValues={{
  ...currentRow,
  redirectUris: currentRow.redirectUris
    ? currentRow.redirectUris.split(',').map(uri => uri.trim()).join('\n')
    : ''
}}
```

#### 前端详情显示（index.tsx）
```typescript
{currentRow.redirectUris?.split(',').map((uri, index) => (
  <div key={index}>{uri.trim()}</div>
))}
```

### 2. authorizationGrantTypes 和 scopes 数组处理

**问题描述**:
- 前端：发送数组 `string[]`
- 后端：需要逗号分隔字符串
- 可能出现 NPE（NullPointerException）

**修复方案**:

#### 后端接收转换（Oauth2RegisteredClientController.java）
```java
// 新增客户端
if (oauth2RegisteredClientDto.getAuthorizationGrantTypes() != null &&
    !oauth2RegisteredClientDto.getAuthorizationGrantTypes().isEmpty()) {
    oauth2RegisteredClient.setAuthorizationGrantTypes(
        String.join(",", oauth2RegisteredClientDto.getAuthorizationGrantTypes())
    );
}

if (oauth2RegisteredClientDto.getScopes() != null &&
    !oauth2RegisteredClientDto.getScopes().isEmpty()) {
    oauth2RegisteredClient.setScopes(
        String.join(",", oauth2RegisteredClientDto.getScopes())
    );
}
```

### 3. 返回数据格式不匹配

**问题描述**:
- 后端数据库存储：逗号分隔字符串
- 前端期望：数组格式（用于显示 Tag）

**修复方案**:

#### 后端 DTO 转换（Oauth2RegisteredClientController.java）
```java
private Oauth2RegisteredClientDto convertToDto(Oauth2RegisteredClient client) {
    if (client == null) {
        return null;
    }

    Oauth2RegisteredClientDto dto = new Oauth2RegisteredClientDto();
    // ... 其他字段

    // 授权类型：逗号分隔字符串转数组
    if (client.getAuthorizationGrantTypes() != null &&
        !client.getAuthorizationGrantTypes().isEmpty()) {
        dto.setAuthorizationGrantTypes(
            Arrays.asList(client.getAuthorizationGrantTypes().split(","))
        );
    }

    // Scopes：逗号分隔字符串转数组
    if (client.getScopes() != null && !client.getScopes().isEmpty()) {
        dto.setScopes(
            Arrays.asList(client.getScopes().split(","))
        );
    }

    // redirectUris：保持逗号分隔格式（前端会处理显示）
    dto.setRedirectUris(client.getRedirectUris());

    return dto;
}
```

### 4. 客户端密钥安全性

**问题描述**:
- 密钥不应该返回给前端
- 即使返回也应该脱敏

**修复方案**:
```java
// 隐藏密钥，只显示是否配置
dto.setClientSecret(
    client.getClientSecret() != null && !client.getClientSecret().isEmpty()
        ? "********"
        : null
);
```

## 数据流转示例

### 新增客户端流程

```
用户输入（前端表单）
├─ authorizationGrantTypes: ["authorization_code", "refresh_token"]
├─ scopes: ["user.read", "user.write"]
└─ redirectUris: "http://localhost:8080/callback\nhttps://app.com/callback"

↓ 前端处理（handleAdd）

前端提交数据
├─ authorizationGrantTypes: ["authorization_code", "refresh_token"]
├─ scopes: ["user.read", "user.write"]
└─ redirectUris: "http://localhost:8080/callback,https://app.com/callback"

↓ 后端接收（Controller.add）

后端转换
├─ authorizationGrantTypes: "authorization_code,refresh_token"
├─ scopes: "user.read,user.write"
└─ redirectUris: "http://localhost:8080/callback,https://app.com/callback"

↓ 数据库存储

数据库记录
├─ authorization_grant_types: "authorization_code,refresh_token"
├─ scopes: "user.read,user.write"
└─ redirect_uris: "http://localhost:8080/callback,https://app.com/callback"
```

### 查询客户端流程

```
数据库查询结果
├─ authorizationGrantTypes: "authorization_code,refresh_token"
├─ scopes: "user.read,user.write"
└─ redirectUris: "http://localhost:8080/callback,https://app.com/callback"

↓ 后端转换（convertToDto）

后端返回 DTO
├─ authorizationGrantTypes: ["authorization_code", "refresh_token"]
├─ scopes: ["user.read", "user.write"]
├─ redirectUris: "http://localhost:8080/callback,https://app.com/callback"
└─ clientSecret: "********"

↓ 前端接收

前端显示（列表）
├─ authorizationGrantTypes: [Tag("authorization_code"), Tag("refresh_token")]
├─ scopes: [Tag("user.read"), Tag("user.write")]
└─ clientSecret: Tag("已配置")

前端显示（详情）
├─ redirectUris:
│   ├─ http://localhost:8080/callback
│   └─ https://app.com/callback
```

### 编辑客户端流程

```
前端获取详情
├─ redirectUris: "http://localhost:8080/callback,https://app.com/callback"

↓ 前端处理（initialValues）

表单回显
└─ redirectUris: "http://localhost:8080/callback\nhttps://app.com/callback"

↓ 用户修改后提交（handleUpdate）

前端提交
└─ redirectUris: "http://localhost:8080/callback,https://newapp.com/callback"

↓ 后端更新
```

## 字段对应关系表

| 前端字段 | 前端类型 | 传输格式 | 后端接收类型 | 存储格式 | 返回格式 |
|---------|---------|---------|------------|---------|---------|
| clientId | string | string | String | string | string |
| clientName | string | string | String | string | string |
| clientSecret | string | string | String | string（加密） | "********" |
| clientAuthenticationMethods | string | string | String | string | string |
| authorizationGrantTypes | string[] | ["a","b"] | List\<String\> | "a,b" | ["a","b"] |
| scopes | string[] | ["a","b"] | List\<String\> | "a,b" | ["a","b"] |
| redirectUris | string | "a,b" | String | "a,b" | "a,b" |

## 修复的文件清单

### 前端文件
1. `frontend-cloud/src/pages/system/OAuth2ClientList/index.tsx`
   - handleAdd 方法：添加 redirectUris 转换逻辑
   - handleUpdate 方法：添加 redirectUris 转换逻辑
   - 编辑表单 initialValues：添加 redirectUris 回显转换
   - 详情抽屉：修复 redirectUris 显示分隔符

### 后端文件
2. `oauth2-server/.../controller/Oauth2RegisteredClientController.java`
   - add 方法：添加空值检查，改用 String.join
   - edit 方法：添加空值检查，改用 String.join
   - convertToDto 方法：添加空值检查，优化转换逻辑

## 测试要点

### 1. 新增客户端测试
- [ ] 填写多个授权类型，保存后检查数据库
- [ ] 填写多个 scopes，保存后检查数据库
- [ ] 填写多行 redirectUris，保存后检查是否正确转换为逗号分隔
- [ ] 不填写 clientSecret（公共客户端），检查是否正常保存

### 2. 编辑客户端测试
- [ ] 编辑已有客户端，检查 redirectUris 是否正确回显为多行
- [ ] 修改授权类型，保存后检查是否正确更新
- [ ] 修改 scopes，保存后检查是否正确更新
- [ ] clientSecret 显示为 "********"

### 3. 查看客户端测试
- [ ] 列表页面正确显示授权类型 Tag
- [ ] 列表页面正确显示 scopes Tag
- [ ] 详情抽屉正确显示多个 redirectUris（每行一个）
- [ ] clientSecret 显示为 "已配置" 或 "无"

### 4. 删除客户端测试
- [ ] 删除单个客户端
- [ ] 批量删除客户端

### 5. 边界情况测试
- [ ] redirectUris 为空
- [ ] authorizationGrantTypes 只选一个
- [ ] scopes 只选一个
- [ ] redirectUris 包含空行（应该被过滤）
- [ ] 字符串前后有空格（应该被 trim）

## 回归测试

### API 接口测试

1. **GET /api/oauth2/registeredClient/list**
   - 响应数据格式正确
   - authorizationGrantTypes 和 scopes 为数组
   - redirectUris 为逗号分隔字符串

2. **GET /api/oauth2/registeredClient/{id}**
   - 响应数据格式正确
   - 各字段类型匹配

3. **POST /api/oauth2/registeredClient**
   - 请求参数格式正确
   - 数据正确保存到数据库

4. **PUT /api/oauth2/registeredClient**
   - 更新逻辑正确
   - 不允许修改 clientId

5. **DELETE /api/oauth2/registeredClient/{ids}**
   - 删除逻辑正确

## 注意事项

1. **数据迁移**
   - 如果已有历史数据，需要检查数据格式是否符合新的转换逻辑

2. **空值处理**
   - 所有数组字段都添加了空值检查
   - 避免 NPE

3. **字符串处理**
   - split 操作后使用 trim 去除空格
   - filter 过滤空字符串

4. **安全性**
   - clientSecret 永远不返回真实值
   - 只显示是否配置

## 后续优化建议

1. **表单验证增强**
   - redirectUris 格式校验（URL 格式）
   - clientId 格式校验（只允许小写字母、数字、连字符）

2. **错误提示优化**
   - 更详细的错误信息
   - 字段级别的错误提示

3. **用户体验优化**
   - redirectUris 输入框提示示例
   - 实时格式校验

4. **数据校验**
   - 后端添加更严格的参数校验
   - 使用 @Valid 注解

## 相关文档

- `OAuth2客户端新增功能使用指南.md` - 后端 Service 层使用指南
- `OAuth2客户端管理功能实现文档.md` - 完整功能文档
