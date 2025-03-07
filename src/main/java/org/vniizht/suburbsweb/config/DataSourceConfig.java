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
    private static final String xmlConfigLocation = "/opt/read/datab/DEFAULTX.XML";
    private static final String primaryXmlName = "models";
    private static final String loggerXmlName = "monitor";

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate() throws NamingException {
        return new JdbcTemplate(getJndiDataSource(primaryJndiDS));
    }

    @Bean
    @Profile("console")
    public JdbcTemplate consoleJdbcTemplate() throws Exception {
        return new JdbcTemplate(getXmlDataSource(primaryXmlName));
    }

    @Bean(name = "ngLoggerJdbcTemplate")
    public JdbcTemplate ngLoggerJdbcTemplate() throws NamingException {
        return new JdbcTemplate(getJndiDataSource(loggerJndiDS));
    }

    @Bean(name = "ngLoggerJdbcTemplate")
    @Profile("console")
    public JdbcTemplate ngLoggerConsoleJdbcTemplate() throws Exception {
        return new JdbcTemplate(getXmlDataSource(loggerXmlName));
    }

    private DataSource getJndiDataSource(String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    private DataSource getXmlDataSource(String xmlName) throws Exception {

        File xmlFile = new File(xmlConfigLocation);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList databases = doc.getElementsByTagName("database");
        for (int i = 0; i < databases.getLength(); i++) {
            Element dbElement = (Element) databases.item(i);
            String name = dbElement.getAttribute("name");

            if (xmlName.equals(name)) {
                String url = getTextContentByTag(dbElement, "url");
                String username = getTextContentByTag(dbElement, "username");
                String password = getTextContentByTag(dbElement, "password");
                String driverClassName = getTextContentByTag(dbElement, "driver-class");

                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setUrl(url);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setDriverClassName(driverClassName);

                return dataSource;
            }
        }
        throw new IllegalArgumentException("Database config '" + xmlName + "' not found in XML");
    }

    private String getTextContentByTag(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            throw new IllegalArgumentException("Missing required tag: " + tagName);
        }
        return nodeList.item(0).getTextContent();
    }
}
