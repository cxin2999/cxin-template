# cxin-template

Spring Boot 3.5 + Vue 3 全栈项目模板，开箱即用的用户系统（注册/登录/权限管理），适合快速启动新项目。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.5.14 |
| ORM | MyBatis-Plus | 3.5.12 |
| 数据库 | MySQL + HikariCP 连接池 | — |
| 缓存/Session | Redis + Spring Session | — |
| 接口文档 | Knife4j (springdoc-openapi) | 4.4.0 |
| 对象映射 | MapStruct | 1.6.0 |
| 工具库 | Hutool | 5.8.43 |
| 前端框架 | Vue 3 + TypeScript | 3.5.x |
| 构建工具 | Vite | 8.x |
| UI 组件库 | Ant Design Vue | 4.x |
| 状态管理 | Pinia | 3.x |
| 路由 | Vue Router | 5.x |
| HTTP 客户端 | Axios | 1.x |

## 项目结构

```
cxin-template/
├── src/main/java/com/cxin/cxintemplate/
│   ├── config/                    # Spring 配置（CORS 等）
│   ├── controller/                # REST 接口层
│   │   ├── HealthController       # 健康检查
│   │   └── UserController         # 用户模块接口（注册/登录/CRUD）
│   ├── generator/                 # MyBatis-Plus 代码生成器
│   ├── infrastructure/            # 基础设施（跨切面层）
│   │   ├── annotation/            # 自定义注解（@AuthCheck 权限校验）
│   │   ├── aspect/                # AOP 切面（AuthInterceptor 权限拦截）
│   │   ├── common/                # 通用类（BaseResponse、ResultUtils、分页请求等）
│   │   ├── constant/              # 常量定义
│   │   ├── convert/               # MapStruct 对象转换器
│   │   ├── enums/                 # 枚举类
│   │   ├── exception/             # 异常处理（全局异常拦截、业务异常、错误码）
│   │   ├── model/                 # 数据模型
│   │   │   ├── dto/               # 请求参数对象
│   │   │   ├── entity/            # 数据库实体（MyBatis-Plus 映射）
│   │   │   └── vo/                # 视图对象（脱敏后返回前端）
│   │   └── utils/                 # 工具类
│   ├── mapper/                    # MyBatis-Plus Mapper 接口
│   └── service/                   # 业务逻辑层
│       └── impl/                  # 接口实现
├── src/main/resources/
│   ├── application.yaml           # 主配置（激活 local 环境）
│   ├── application-local.yaml     # 本地开发环境配置（数据库/Redis 连接）
│   └── mapper/                    # MyBatis XML 映射文件
├── cxin-frontend/                 # 前端项目
│   └── src/
│       ├── access.ts              # 路由守卫（全局权限校验）
│       ├── api/                   # API 调用函数（由 openapi2ts 自动生成）
│       ├── components/            # 公共组件（全局头部、底部）
│       ├── config/                # 配置（API 地址等）
│       ├── constants/             # 常量（用户角色等）
│       ├── layouts/               # 布局组件
│       ├── pages/                 # 页面组件
│       ├── request.ts             # Axios 实例（拦截器、错误处理）
│       ├── router/                # 路由配置
│       ├── stores/                # Pinia 状态管理
│       ├── styles/                # 全局样式
│       └── utils/                 # 工具函数（权限判断等）
└── pom.xml                        # Maven 依赖配置
```

## 内置功能

- **用户注册/登录/登出** — 密码加盐 MD5 加密，Session 登录态管理
- **用户 CRUD** — 增删改查 + 分页查询，含权限控制
- **权限校验** — `@AuthCheck` 注解声明式鉴权，AOP 切面拦截，支持 user/admin 角色
- **统一响应格式** — `BaseResponse<T>` 封装 code/data/message，`ResultUtils` 统一返回
- **全局异常处理** — 自动捕获业务异常、参数校验异常，统一返回错误格式
- **参数校验** — Jakarta Validation，DTO 注解声明校验规则
- **逻辑删除** — MyBatis-Plus `@TableLogic`，数据不物理删除
- **雪花 ID** — MyBatis-Plus `ASSIGN_ID` 策略生成分布式唯一 ID
- **API 文档** — Knife4j 自动生成，启动后端即可访问
- **自动生成 API 客户端** — 后端写好 Controller 后，前端一键生成类型安全的调用函数
- **数据库代码生成器** — MyBatis-Plus Generator 从表结构生成 entity/mapper/service/controller
- **前后端分离** — Vite 开发代理，Session Cookie 携带，CORS 配置

## 内置 API 接口

### 无需登录

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/health/` | 健康检查 |
| POST | `/api/user/register` | 用户注册 |
| POST | `/api/user/login` | 用户登录 |
| GET | `/api/user/get/login` | 获取当前登录用户信息 |
| GET | `/api/user/logout` | 用户登出 |
| GET | `/api/user/get/vo` | 根据 ID 获取用户公开信息 |

### 需要管理员权限

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/add` | 创建用户（默认密码 12345678） |
| GET | `/api/user/get` | 根据 ID 获取用户完整信息 |
| POST | `/api/user/delete` | 删除用户（逻辑删除） |
| POST | `/api/user/update` | 更新用户信息 |
| POST | `/api/user/list/page/vo` | 分页查询用户列表 |

## 快速开始

### 1. 环境要求

- JDK 21+
- Node.js 20.19+ 或 22.12+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.8+（项目自带 Maven Wrapper，无需手动安装）

### 2. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS cxintemplate DEFAULT CHARACTER SET utf8mb4;
```

### 3. 配置数据库连接

编辑 `src/main/resources/application-local.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cxintemplate?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 你的密码
  data:
    redis:
      host: localhost
      port: 6379
      password: 你的Redis密码
      database: 0
```

### 4. 初始化用户表

在数据库中执行以下建表语句：

```sql
CREATE TABLE `user` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `userAccount` varchar(256) NOT NULL COMMENT '账号',
  `userPassword` varchar(512) NOT NULL COMMENT '密码',
  `userName` varchar(256) DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) DEFAULT 'user' COMMENT '用户角色：user/admin',
  `editTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userAccount` (`userAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 5. 启动后端

```bash
# 使用 Maven Wrapper（无需安装 Maven）
./mvnw spring-boot:run
```

后端启动在 **http://localhost:8567/api**，Knife4j 接口文档在 **http://localhost:8567/api/doc.html**。

### 6. 启动前端

```bash
cd cxin-frontend
npm install
npm run dev
```

前端启动在 **http://localhost:5173**，开发代理自动转发 `/api` 请求到后端。

### 7. 生成前端 API 客户端（可选）

```bash
# 确保后端正在运行
cd cxin-frontend
npm run openapi2ts
```

该命令读取后端 `/api/v3/api-docs` 接口文档，自动在 `src/api/` 下生成类型安全的 API 调用函数。

## 使用模板后需要做的事

clone 此模板后，按以下清单完成初始化：

- [ ] 修改 `pom.xml` 中的 `groupId`、`artifactId`、`name`
- [ ] 修改 `application.yaml` 中的 `spring.application.name`
- [ ] 修改 `application-local.yaml` 中的数据库/Redis 连接信息和库名
- [ ] 修改 `EncryptUtils.java` 中的 `PASSWORD_SALT`（密码加盐值）
- [ ] 修改 `CxinTemplateApplication.java` 的类名和包名（按需）
- [ ] 修改 `vite.config.ts` 中 `/api` 代理的 `target` 端口（如修改了后端端口）
- [ ] 修改 `openapi2ts.config.ts` 中的 `schemaPath` 地址
- [ ] 修改前端 `package.json` 中的 `name`
- [ ] 修改前端 `index.html` 中的页面标题

## 开发指南

### 后端

```bash
# 编译
./mvnw compile

# 运行测试
./mvnw test

# 运行单个测试类
./mvnw test -Dtest=你的测试类名

# 打包（跳过测试）
./mvnw package -DskipTests
```

**代码生成器**：编辑 `CodeGenerator.java` 中的数据库连接和表名，运行 `main` 方法即可生成 entity/mapper/service/controller。

**新增业务模块**的典型步骤：
1. 数据库建表
2. 运行 CodeGenerator 生成基础代码
3. 定义 DTO（请求参数）和 VO（返回对象）
4. 编写 MapStruct Convert 接口做对象转换
5. 在 Service 中实现业务逻辑
6. 在 Controller 上添加 `@AuthCheck` 控制权限（如需要）
7. 在 Controller 方法上添加 `@Operation` 注解（供 openapi2ts 生成前端代码）

### 前端

```bash
cd cxin-frontend

# 开发模式
npm run dev

# 类型检查
npm run type-check

# 格式化
npm run format

# Lint 检查并自动修复
npm run lint
```

**新增页面**的典型步骤：
1. 在 `src/pages/` 下新建页面组件
2. 在 `src/router/index.ts` 中添加路由
3. 如需权限控制，在 `src/access.ts` 的 `router.beforeEach` 中添加路径判断
4. 如后端有新接口，运行 `npm run openapi2ts` 生成类型安全的调用函数

## 架构说明

### 统一响应格式

所有接口返回如下 JSON 结构：

```json
{
  "code": 0,
  "data": { ... },
  "message": "ok"
}
```

错误码定义在 `ErrorCode` 枚举中：`0` 成功、`40000` 参数错误、`40100` 未登录、`40101` 无权限、`40400` 数据不存在、`50000` 系统异常。

### 权限校验

在 Controller 方法上使用 `@AuthCheck(mustRole = "admin")` 注解声明权限要求。`AuthInterceptor`（AOP 切面）自动从 Session 中读取当前用户并进行角色比对。不需要权限的方法不加注解即可。

### 对象转换

使用 MapStruct 做 Entity ↔ VO 转换，禁止手动逐字段赋值。转换规则定义在 `infrastructure/convert/` 包下。编译时 MapStruct 自动生成实现类（确保 `maven-compiler-plugin` 中配置了 MapStruct 的 annotation processor）。

### 前端 API 调用

```typescript
import { userLogin } from '@/api/userController'

const res = await userLogin({ userAccount: 'admin', userPassword: '12345678' })
if (res.data.code === 0) {
  // res.data.data 类型为 API.LoginUserVO，有完整的 TypeScript 类型提示
}
```

`openapi2ts` 从后端的 Swagger 文档自动生成所有 API 函数和类型定义，无需手写。

### 前端权限流程

1. `main.ts` 应用启动 → 调用 `fetchLoginUser()` 获取当前登录用户
2. `access.ts` 路由守卫 → 每次页面跳转时校验权限（管理员路由以 `/admin` 开头）
3. `request.ts` 响应拦截器 → 任何接口返回 40100（未登录）时自动跳转登录页
