/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.scheduler.test.db;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import cn.ponfee.scheduler.common.base.exception.CheckedThrowing;
import cn.ponfee.scheduler.common.util.Files;
import cn.ponfee.scheduler.common.util.MavenProjects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import static cn.ponfee.scheduler.test.db.DBTools.DB_NAME;

/**
 * MariaDB Server
 * <p>SELECT VERSION()  ->  10.2.11-MariaDB
 *
 * @author Ponfee
 */
public class EmbeddedMysqlServerMariaDB {

    public static void main(String[] args) throws Exception {
        DB db = start(3306);
        Runtime.getRuntime().addShutdownHook(new Thread(CheckedThrowing.runnable(db::stop)));
    }

    public static DB start(int port) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:" + port + "/" + DB_NAME;

        String dataDir = createDataDirectory();

        DBConfiguration configuration = DBConfigurationBuilder.newBuilder()
            .setPort(port) // OR, default: setPort(0); => autom. detect free port
            .setDataDir(dataDir) // just an example
            //.addArg("--skip-grant-tables") // 默认就是skip-grant-tables
            .build();
        DB db = DB.newEmbeddedDB(configuration);

        System.out.println("Embedded maria db starting...");
        db.start();
        System.out.println("Embedded maria db started!");

        db.source(IOUtils.toInputStream(loadScript(), StandardCharsets.UTF_8));
        JdbcTemplate jdbcTemplate = DBTools.createJdbcTemplate(jdbcUrl, DB_NAME, DB_NAME);

        System.out.println("\n--------------------------------------------------------testDatabase");
        DBTools.testNativeConnection("com.mysql.cj.jdbc.Driver", jdbcUrl, DB_NAME, DB_NAME);

        System.out.println("\n--------------------------------------------------------testMysql");
        DBTools.testMysql(jdbcTemplate);

        System.out.println("\n--------------------------------------------------------testJdbcTemplate");
        DBTools.testJdbcTemplate(jdbcTemplate);

        System.out.println("\n--------------------------------------------------------testQuerySql");
        DBTools.testQuerySchedJob(jdbcTemplate);
        return db;
    }

    private static String loadScript() throws Exception {
        return Arrays.stream(DBTools.loadScript().split("\n"))
            // fix error: The MariaDB server is running with the --skip-grant-tables option so it cannot execute this statement
            .filter(s -> !StringUtils.startsWithAny(s, "CREATE USER ", "GRANT ALL PRIVILEGES ON ", "FLUSH PRIVILEGES;"))
            .collect(Collectors.joining("\n"));
    }

    private static String createDataDirectory() throws IOException {
        String dataDir = MavenProjects.getProjectBaseDir() + "/target/mariadb/";
        File file = new File(dataDir);
        if (file.exists()) {
            PathUtils.deleteDirectory(file.toPath());
        }
        Files.mkdir(file);
        return dataDir;
    }

}