package com.gdschongik.gdsc.helper;

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

    private void extractTableNames(Connection conn) {
        tableNames = em.getMetamodel().getEntities().stream()
                .map(DatabaseCleaner::getTableName)
                .toList();
    }

    private static String getTableName(EntityType<?> entity) {
        if (entity.getName().equals("Order")) {
            return "orders";
        }
        return convertCamelCaseToSnakeCase(entity.getName());
    }

    /**
     * 카멜 케이스로 되어있는 엔티티 이름을 스네이크 케이스로 되어있는 테이블 이름으로 변환한다.
     */
    private static String convertCamelCaseToSnakeCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
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
