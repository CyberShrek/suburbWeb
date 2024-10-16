package org.vniizht.suburbsweb;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DynamicSchemaNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Value("${schemas.prefix}")
    private String schemasPrefix;
    @Value("${schemas.ignore}")
    private String schemasIgnore;

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if(identifier != null && Arrays.stream(schemasIgnore.split("\\s*,\\s*")).noneMatch(identifier.getCanonicalName()::startsWith)) {
            identifier = super.getIdentifier(schemasPrefix + identifier.getCanonicalName(), false, jdbcEnvironment);
        }
        return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
    }
}
