package org.vniizht.suburbsweb.config;

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
    private static final String xmlConfigLocation = "/home/master/Development/projects/suburbWeb/DEFAULTX.XML";
//    private static final String xmlConfigLocation = "/opt/read/datab/DEFAULTX.XML";
    private static final String primaryXmlDS = "NGDS";
    private static final String loggerXmlDS = "LogDS";

    @Bean(name = "jdbcTemplate")
    @Primary
    @Profile("war")
    public JdbcTemplate jdbcTemplate() throws NamingException {
        System.out.println("primaryJndiDS = " + primaryJndiDS);
        return new JdbcTemplate(getJndiDataSource(primaryJndiDS));
    }

    @Bean(name = "ngLoggerJdbcTemplate")
    @Profile("war")
    public JdbcTemplate ngLoggerJdbcTemplate() throws NamingException {
        System.out.println("loggerJndiDS = " + loggerJndiDS);
        return new JdbcTemplate(getJndiDataSource(loggerJndiDS));
    }

    @Bean(name = "jdbcTemplate")
    @Primary
    @Profile("jar")
    public JdbcTemplate consoleJdbcTemplate() throws Exception {
        System.out.println("primaryXmlDS = " + primaryXmlDS);
        return new JdbcTemplate(getXmlDataSource(primaryXmlDS));
    }

    @Bean(name = "ngLoggerJdbcTemplate")
    @Profile("jar")
    public JdbcTemplate ngLoggerConsoleJdbcTemplate() throws Exception {
        System.out.println("loggerXmlDS = " + loggerXmlDS);
        return new JdbcTemplate(getXmlDataSource(loggerXmlDS));
    }

    private DataSource getJndiDataSource(String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    private DataSource getXmlDataSource(String dsName) throws Exception {

        System.out.println("dsName = " + dsName);

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
}
