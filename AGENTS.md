# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Project Overview

A Spring Boot 3.5 monorepo template with a Vue 3 frontend. The backend uses MyBatis-Plus for persistence, Redis-backed Spring Session for authentication, and Knife4j for API documentation. The frontend uses Ant Design Vue 4, Pinia, and Vue Router 5.

## Build & Run

### Backend (Java 21, Maven)

```bash
# Build
./mvnw compile

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Start (on port 8567, context-path /api)
./mvnw spring-boot:run

# Package
./mvnw package -DskipTests
```

### Frontend (Node >=20.19, Vite 8)

```bash
cd cxin-frontend

# Install
npm install

# Dev server (with hot reload, proxies /api → localhost:8567)
npm run dev

# Build for production
npm run build

# Type checking
npm run type-check

# Lint (oxlint + eslint)
npm run lint

# Format
npm run format
```

### API Code Generation (Frontend)

```bash
cd cxin-frontend
# Start backend first, then generate TS API clients from OpenAPI spec
npm run openapi2ts
```

This reads the backend's `/api/v3/api-docs` endpoint (Knife4j/springdoc) and auto-generates typed API client functions under `src/api/`. The backend's controller `@Operation(operationId=...)` annotations map to generated function names.

### Database Code Generation (Backend)

Run `CodeGenerator.main()` in `src/main/java/com/cxin/cxintemplate/generator/CodeGenerator.java`. It uses MyBatis-Plus Generator with Velocity templates to produce entity, mapper, service, and controller stubs from a database table. Configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `TABLE_NAME` constants first.

## Architecture

### Backend Package Structure

```
com.cxin.cxintemplate
├── config/              # Spring config (CORS)
├── controller/          # REST controllers
├── generator/           # MyBatis-Plus code generator
├── infrastructure/      # Cross-cutting infrastructure
│   ├── annotation/      # Custom annotations (@AuthCheck)
│   ├── aspect/          # AOP aspects (AuthInterceptor)
│   ├── common/          # BaseResponse, ResultUtils, PageRequest, DeleteRequest
│   ├── constant/        # Static constants (UserConstant)
│   ├── convert/         # MapStruct converters (entity ↔ VO ↔ DTO)
│   ├── enums/           # Enumerations (UserRoleEnum)
│   ├── exception/       # BusinessException, ErrorCode, GlobalExceptionHandler, ThrowUtils
│   ├── model/
│   │   ├── dto/user/    # Request DTOs per feature
│   │   ├── entity/      # MyBatis-Plus entities (map to DB tables)
│   │   └── vo/user/     # Response VOs (sanitized for clients)
│   └── utils/           # EncryptUtils, ParseJsonUtils
├── mapper/              # MyBatis-Plus mapper interfaces (extend BaseMapper)
└── service/
    └── impl/            # Service interfaces + implementations (extend IService/ServiceImpl)
```

### Key Patterns

**Unified API Response**: Every controller returns `BaseResponse<T>` via `ResultUtils.success(data)`. Error responses use `ResultUtils.error(ErrorCode)`. The `ErrorCode` enum defines all error codes (0 = success, 40000 = params error, 40100 = not logged in, 40101 = no auth, 40400 = not found, 50000 = system error, 50001 = operation error).

**Auth**: Session-based via Redis. `@AuthCheck(mustRole = "admin")` on controller methods triggers `AuthInterceptor` (AOP around-advice). The interceptor reads the current user from `request.getSession()` and compares roles. Login state is stored under `UserConstant.USER_LOGIN_STATE`. Session/cookie timeout is 30 days (2,592,000 seconds).

**Entity Mapping**: MapStruct interfaces in `infrastructure/convert/` handle entity ↔ VO conversions. Do not manually copy properties — add a method to the converter instead.

**Soft Delete**: MyBatis-Plus `@TableLogic` on the `isDelete` field. Queries and deletes automatically filter out soft-deleted records.

**Validation**: Controller classes annotated with `@Validated`, request bodies with `@Valid`. DTO fields use Jakarta validation annotations. `GlobalExceptionHandler` catches `MethodArgumentNotValidException` and returns a 40000 error.

**ID Generation**: MyBatis-Plus `IdType.ASSIGN_ID` (snowflake-style string IDs).

### Frontend Structure

```
cxin-frontend/src
├── access.ts            # Router guard for auth/permission checks
├── api/                 # Auto-generated API client functions (openapi2ts)
├── components/          # Shared components (GlobalHeader, GlobalFooter)
├── config/              # Environment config (API_BASE_URL)
├── constants/           # User role constants
├── layouts/             # BasicLayout (header + content + footer)
├── pages/               # Page components per route
├── request.ts           # Axios instance with interceptors
├── router/              # Vue Router config
├── stores/              # Pinia stores (loginUser)
├── styles/              # Global CSS (common.css, variables.css)
└── utils/               # Permission utilities
```

**Auth Flow (Frontend)**: On app startup (`main.ts`), `loginUserStore.fetchLoginUser()` calls `GET /user/get/login`. The router guard in `access.ts` re-checks on navigation. The axios interceptor in `request.ts` redirects to login if any API returns code 40100. Admin routes (paths starting with `/admin`) require `USER_ROLE_ADMIN`.

**API Client Pattern**: Generated functions in `src/api/` accept typed request bodies and return `request<API.BaseResponseXxx>(...)`. They're auto-generated — do not hand-edit them. If you add a new backend endpoint, re-run `npm run openapi2ts` to regenerate.

## Configuration

- **Active profile**: `local` (set in `application.yaml`). Local overrides are in `application-local.yaml`.
- **Database**: MySQL via HikariCP connection pool. Configure in `application-{profile}.yaml`.
- **Redis**: Used for session storage (`spring-session-data-redis`). Configure host/port/password per profile.
- **API Docs**: Knife4j enabled at `/api/doc.html` (when running backend).
- **CORS**: Configured in `CorsConfig` with `allowCredentials(true)` and `allowedOriginPatterns("*")`.

## Dependencies to Know

- **Hutool** (`cn.hutool`): All-in-one Java utility library. Used for string checks (`StrUtil`), bean copying (`BeanUtil`), etc. Prefer Hutool methods over writing manual utilities.
- **MyBatis-Plus**: Extends MyBatis with ActiveRecord-style CRUD via `BaseMapper<T>` / `IService<T>` / `ServiceImpl<M, T>`. Use `QueryWrapper<T>` for dynamic queries.
- **MapStruct**: Compile-time bean mapping. Add methods to interfaces in `infrastructure/convert/` — the `maven-compiler-plugin` annotation processor generates implementations.
- **Ant Design Vue 4**: Component library. Import components individually or rely on the global registration in `main.ts`.
