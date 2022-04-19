package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLHelper {
    private final ConnectionFactory connectionFactory;

    public SQLHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void execute(String sqlStatement) {
        execute(sqlStatement, PreparedStatement::execute);
    }

    public <T> T execute(String sqlStatement, StatementExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            return executor.execute(preparedStatement);
        } catch (SQLException e) {
            throw SQLConverterException.convert(e);
        }
    }

    public <T> T transactionalExecute(SQLTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw SQLConverterException.convert(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
