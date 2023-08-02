package com.wuhobin;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

import java.util.Collections;

/**
 * mybatis-plus代码生成器
 *
 * @author wuhongbin
 */
public class CodeGeneratorPlus {

    private static String jdbcUrl = "jdbc:mysql://rm-bp1j8k9s2330jz5jdyo.mysql.rds.aliyuncs.com/diychat?useUnicode=true&characterEncoding=UTF-8";
    private static String jdbcUsername = "root";
    private static String jdbcPassword = "Whb18772916901";

    /**
     * 父级包名配置
     */
    private static String parentPackage = "com.wuhobin";

    /**
     * 项目业务module
     */
    private static String moduleName = "diychat-domain";

    /**
     * 生成代码的 @author 值
     */
    private static String author = "wuhongbin";

    /**
     * 项目地址[改为自己本地项目位置]
     * D:/code/after-end/wechat-activity-platform/wechat-activity-platform-project/by-card-lottery/
     */
    private static String projectPath = "D:/idea_wuhongbin/springboot_study/DiyChat/DiyChat";

    /**
     * 要生成代码的表名配置
     */
    private static String[] tables = {
        "t_user_info"
    };

    private static final DataSourceConfig.Builder dataSourceConfig = new DataSourceConfig
            .Builder(jdbcUrl,jdbcUsername,jdbcPassword)
            .typeConvert(new MySqlTypeConvert() {
                @Override
                public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                    /**
                     *   tinyint转换成Boolean
                     */
                    if (fieldType.toLowerCase().contains("tinyint")) {
                        return DbColumnType.BOOLEAN;
                    }
                    /**
                     *  将数据库中datetime,date,timestamp转换成date
                     */
                    if (fieldType.toLowerCase().contains("datetime") || fieldType.toLowerCase().contains("date") || fieldType.toLowerCase().contains("timestamp")) {
                        return DbColumnType.DATE;
                    }
                    return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                }
            });

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        //1、配置数据源
        FastAutoGenerator.create(dataSourceConfig)
                //2、全局配置
                .globalConfig(builder -> {
                    builder
                            .outputDir(projectPath +"/"+ moduleName + "/src/main/java")  //指定输出目录
                            .author(author)    // 作者名
//                            .disableOpenDir()  // 禁止打开输出目录
                            .dateType(DateType.ONLY_DATE) //时间策略
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .enableSwagger();  //开启 swagger 模式
                })  //3、包配置
                .packageConfig(builder -> {
                    builder
                            .parent(parentPackage)  // 父包名
//                            .moduleName(moduleName) // 父包模块名
                            .entity("dataobject")   // Entity 包名
                            .mapper("mapper")    // Mapper 包名
                            .service("service")    //  Service 包名
                            .serviceImpl("service.impl") //Service Impl 包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/" + moduleName + "/src/main/resources/sqlmap/mapper"));
                }).templateConfig(builder -> {
                    builder
                            //.mapperXml(null)
                            //.entity(null)
//                            .service(null)
//                            .serviceImpl(null)
                            .controller(null);// 不生成controller
                })
                .strategyConfig(builder -> {
                    builder
                            .enableCapitalMode()    //开启大写命名
                            .enableSkipView()   //创建实体类的时候跳过视图
                            .addInclude(tables)  // 设置需要生成的数据表名
                            .addTablePrefix("t")  //设置 过滤 表的前缀
                            .mapperBuilder()
                            .enableBaseColumnList()
                            .enableBaseResultMap()
                            .entityBuilder().enableLombok().formatFileName("%sDO");

                })
                .execute();

    }
}
