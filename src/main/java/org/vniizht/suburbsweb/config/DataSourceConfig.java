package org.vniizht.suburbsweb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Configuration
public class DataSourceConfig {

    private static final String primaryJndiDS = "java:/ModelsDS";
    private static final String loggerJndiDS = "java:/LogDS";
    private static final String xmlConfigLocation = "/opt/read/datab/DEFAULTX.XML";
    private static final String primaryXmlDS = "NGDS";
    private static final String loggerXmlDS = "LogDS";

    @Bean
//    @Profile("war")
    public DataSource dataSource() throws NamingException {
        return getJndiDataSource(primaryJndiDS);
    }

//    @Bean
//    @Profile("jar")
//    public DataSource consoleDataSource() throws Exception {
//        return getXmlDataSource(primaryXmlDS);
//    }

    @Bean(name = "jdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource primaryDataSource) {
        return new JdbcTemplate(primaryDataSource);
    }

    @Bean(name = "ngLoggerJdbcTemplate")
//    @Profile("war")
    public JdbcTemplate ngLoggerJdbcTemplate() throws NamingException {
        return new JdbcTemplate(getJndiDataSource(loggerJndiDS));
    }

//    @Bean(name = "ngLoggerJdbcTemplate")
//    @Profile("jar")
//    public JdbcTemplate ngLoggerConsoleJdbcTemplate() throws Exception {
//        return new JdbcTemplate(getXmlDataSource(loggerXmlDS));
//    }

    private DataSource getJndiDataSource(String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    private DataSource getXmlDataSource(String dsName) throws Exception {

        File xmlFile = new File(xmlConfigLocation);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList tasks = doc.getElementsByTagName("task");
        for (int i = 0; i < tasks.getLength(); i++) {
            Element task = (Element) tasks.item(i);
            if (dsName.equals(task.getAttribute("datasource"))) {
                return getHikariDataSource(task);
            }
        }
        throw new IllegalArgumentException("Database config '" + dsName + "' not found in XML");
    }

    private static DataSource getHikariDataSource(Element task) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(task.getAttribute("jdbcString"));
        config.setUsername(task.getAttribute("nameuser"));
        config.setPassword(task.getAttribute("pass"));
        config.setDriverClassName(task.getAttribute("driverName"));

        // Основные настройки
        config.setMaximumPoolSize(5);               // Максимум 5 соединений
        config.setMinimumIdle(1);                   // Минимум 1 соединение в простое
        config.setConnectionTimeout(5000);          // 5 секунд на получение соединения
        config.setLeakDetectionThreshold(1_800_000);// Детекция утечек после 30 минут
        config.setIdleTimeout(300_000);             // 5 минут неактивности до закрытия
        config.setMaxLifetime(3_600_000);           // 60 минут макс. время жизни соединения
        config.setInitializationFailTimeout(0);     // Немедленный fail при ошибке подключения

        // Оптимизация для быстрого старта
        config.setPoolName("BatchPool");
        config.setRegisterMbeans(false);            // Отключаем JMX для краткоживущих приложений

        // Настройки для высокопроизводительных операций
        //        config.setAutoCommit(true);                 // Автоматическое управление транзакциями
        //        config.setIsolateInternalQueries(true);     // Изолировать внутренние запросы пула
        //        config.setAllowPoolSuspension(false);       // Не требуется для однопоточного доступа

        // Оптимизация под "burst" нагрузку
        config.setLeakDetectionThreshold(300_000);   // Детекция утечек после 300 сек

        // Кэширование PreparedStatements:
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");

        return new HikariDataSource(config);
    }
}
