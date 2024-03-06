package com.gdschongik.gdsc.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        em.unwrap(Session.class).doWork(this::extractTableNames);
    }

    private void extractTableNames(Connection conn) throws SQLException {
        tableNames = em.getMetamodel().getEntities().stream()
                .map(EntityType::getName)
                .toList();
    }

    public void execute() {
        em.unwrap(Session.class).doWork(this::cleanTables);
    }

    private void cleanTables(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (String name : tableNames) {
            statement.executeUpdate(String.format("TRUNCATE TABLE %s", name));
            statement.executeUpdate(String.format("ALTER TABLE %s ALTER COLUMN %s_id RESTART WITH 1", name, name));
        }

        statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
