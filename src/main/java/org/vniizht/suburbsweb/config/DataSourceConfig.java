package org.vniizht.suburbsweb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Properties;

@Configuration
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        JdbcTemplateAutoConfiguration.class,
//})
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = {
//                "org.vniizht.suburbsweb.service.data.repository.level2",
//                "org.vniizht.suburbsweb.service.data.repository.level3",
//                "org.vniizht.suburbsweb.service.handbook"
//        },
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager"
//)
public class DataSourceConfig {

    private static final String primaryJndiDS = "java:/ModelsDS";
    private static final String loggerJndiDS = "java:/LogDS";
    private static final String xmlConfigLocation = "/opt/read/datab/DEFAULTX.XML";
    private static final String primaryXmlDS = "NGDS";
    private static final String loggerXmlDS = "LogDS";

    @Bean
    @Profile("war")
    public DataSource dataSource() throws NamingException {
        return getJndiDataSource(primaryJndiDS);
    }

    @Bean
    @Profile("jar")
    public DataSource consoleDataSource() throws Exception {
        return getXmlDataSource(primaryXmlDS);
    }

    @Bean(name = "jdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource primaryDataSource) {
        return new JdbcTemplate(primaryDataSource);
    }

//    @Bean(name = "ngLoggerJdbcTemplate")
////    @Profile("war")
//    public JdbcTemplate ngLoggerJdbcTemplate() throws NamingException {
//        return new JdbcTemplate(getJndiDataSource(loggerJndiDS));
//    }

    @Bean(name = "ngLoggerJdbcTemplate")
    @Profile("jar")
    public JdbcTemplate ngLoggerConsoleJdbcTemplate() throws Exception {
        return new JdbcTemplate(getXmlDataSource(loggerXmlDS));
    }

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
                return getDriverManagerDataSource(task);
            }
        }
        throw new IllegalArgumentException("Database config '" + dsName + "' not found in XML");
    }

    private static DriverManagerDataSource getDriverManagerDataSource(Element task) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(              task.getAttribute("jdbcString"));
        dataSource.setUsername(         task.getAttribute("nameuser"));
        dataSource.setPassword(         task.getAttribute("pass"));
        dataSource.setDriverClassName(  task.getAttribute("driverName"));
        return dataSource;
    }

//    @Bean(name = "entityManagerFactory") // Явное указание имени бина
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            DataSource dataSource) {
//
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("org.vniizht.suburbsweb.model"); // Укажите пакет с Entity-классами
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//
//        Properties properties = new Properties();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.put("hibernate.hbm2ddl.auto", "validate"); // или update, если нужно
//        properties.put("hibernate.show_sql", "true");
//        em.setJpaProperties(properties);
//
//        return em;
//    }
}
