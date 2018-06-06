/*
 * Copyright 2010-2018 Boxfuse GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by fangbin.bj on 2018/6/5.
 */
public class FlywayApp {


    // 读取数据库配置参数
    private static Properties config = new Properties();
    static {
        try {
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("activerecord.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 执行数据库版本升级
    public static void migration() {
        // Create the Flyway instance
        Flyway flyway = new Flyway();

        // Point it to the database
        flyway.setDataSource(config.getProperty("com.et.ar.ActiveRecordBase.url"), config.getProperty("com.et.ar.ActiveRecordBase.username"), config.getProperty("com.et.ar.ActiveRecordBase.password"),null);
//        flyway.setInitOnMigrate(true);
//        flyway.setSqlMigrationPrefix("ibase");
        flyway.setSqlMigrationPrefix("V");
//        flyway.setSqlMigrationPrefix("iview");

        // Start the migration
//        flyway.setBaselineDescription("iview");
//        flyway.setValidateOnMigrate(true);
//        flyway.clean();
        flyway.setOutOfOrder(true);
//        flyway.setLocations("");
//        flyway.setCleanDisabled(true);
//        flyway.setCleanOnValidationError(true);
//        flyway.setIgnoreFutureMigrations(false);
//        flyway.setIgnoreFailedFutureMigration(true);
        flyway.migrate();
    }

}
