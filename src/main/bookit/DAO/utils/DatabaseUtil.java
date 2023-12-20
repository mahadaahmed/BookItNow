package main.bookit.DAO.utils;

import main.bookit.DAO.UserDAO;
import main.bookit.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    public static Connection getConnection() throws SQLException {
        return Config.getInstance().getConnection();
    }

    public static void closeQuietly(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            // Log or handle close exception
            logger.error("Something went wrong when closing...", e);
        }
    }

    public static <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = createPreparedStatement(conn, sql, params);
            rs = pstmt.executeQuery();
            return handler.handle(rs);
        } catch (SQLException e) {
            // Log and handle exception
            logger.error("Something went wrong when executing the sql...", e);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(rs);
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
    }

    public static int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = createPreparedStatement(conn, sql, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            // Log and handle exception
            logger.error("Something went wrong when executing and updating the database...", e);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
    }

    private static PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }

    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
}
