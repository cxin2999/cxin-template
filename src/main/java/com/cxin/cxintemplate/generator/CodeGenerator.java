package com.cxin.cxintemplate.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus 代码生成器
 * 生成 user 表对应的 entity、mapper、service、controller
 */
public class CodeGenerator {
//todo init
    // ========== 数据库配置 ==========
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cxintemplate" +
            "?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    // ========== 项目配置 ==========
    /**
     * 项目根包名
     */
    private static final String PARENT_PACKAGE = "com.cxin.cxintemplate";
    private static final String TABLE_NAME = "product";

    /**
     * 代码输出根目录（genresult 包所在的 java 源码目录）
     * 生成的 Java 文件会放到：
     * …/generator/genresult/entity/
     * …/generator/genresult/mapper/
     * …/generator/genresult/service/
     * …/generator/genresult/controller/
     */
    private static final String OUTPUT_DIR = System.getProperty("user.dir")
            + "/src/main/java";

    /**
     * 生成代码所在的子包（相对于 PARENT_PACKAGE）
     */
    private static final String MODULE_NAME = "generator.genresult";

    /**
     * Mapper XML 输出目录
     */
    private static final String XML_OUTPUT_DIR = System.getProperty("user.dir")
            + "/src/main/resources/mapper";

    public static void main(String[] args) {
        FastAutoGenerator.create(DB_URL, DB_USERNAME, DB_PASSWORD)

                // ── 全局配置 ──────────────────────────────────────────────
                .globalConfig(builder -> builder
                        .author("Charles Chen")
                        .outputDir(OUTPUT_DIR)
                        // 生成后不自动打开输出目录
                        .disableOpenDir()
                )

                // ── 包配置 ────────────────────────────────────────────────
                .packageConfig(builder -> builder
                        .parent(PARENT_PACKAGE)
                        .moduleName(MODULE_NAME)
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        // Mapper XML 单独输出到 resources/mapper/genresult
                        .pathInfo(Collections.singletonMap(
                                OutputFile.xml, XML_OUTPUT_DIR))
                )

                // ── 策略配置 ──────────────────────────────────────────────
                .strategyConfig(builder -> builder
                        // 只生成 chat_history 表
                        .addInclude(TABLE_NAME)

                        // ---- Entity ----
                        .entityBuilder()
                        // 命名策略：下划线转驼峰
                        .naming(NamingStrategy.underline_to_camel)
                        // 字段命名策略：不转换（保持数据库字段名原样，满足"属性名与字段名一致"的要求）
                        .columnNaming(NamingStrategy.no_change)
                        // 开启 Lombok
                        .enableLombok()
                        // 逻辑删除字段
                        .logicDeleteColumnName("isDelete")
                        .logicDeletePropertyName("isDelete")
                        // 开启 TableField 注解（字段名与属性名相同时也生成注解，便于维护）
                        .enableTableFieldAnnotation()

                        // ---- Mapper ----
                        .mapperBuilder()
                        // 生成 @Mapper 注解
                        .enableMapperAnnotation()

                        // ---- Service ----
                        .serviceBuilder()
                        // 去掉默认的 "I" 前缀，接口名直接用 UserService
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")

                        // ---- Controller ----
                        .controllerBuilder()
                        // 开启 @RestController
                        .enableRestStyle()
                        // 开启驼峰转连字符的 URL 映射（如 /user-info）
                        .enableHyphenStyle()
                )

                // 使用 Velocity 模板引擎（默认，需引入 velocity-engine-core 依赖）
                .templateEngine(new VelocityTemplateEngine())

                .execute();

        System.out.println("✅ 代码生成完毕！输出目录：" + OUTPUT_DIR);
    }
}
