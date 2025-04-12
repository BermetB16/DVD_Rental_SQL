import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;

public class DB_Connection {

    private static Connection connection;
    private static Statement statement;

    private DB_Connection(){

    }
    //dvdrental
    private static PGSimpleDataSource getBaseDataSource(String database) {
        return new PGSimpleDataSource(){{
            setServerName(ConfigReader.getValue("server"));
            setPortNumber(Integer.parseInt(ConfigReader.getValue("port")));
            setUser(ConfigReader.getValue("user"));
            setPassword(ConfigReader.getValue("password"));
            setDatabaseName(database);
        }};
    }

    public static void openConnection(String database)  {
        if (connection == null) {
            try {
                connection = getBaseDataSource(database).getConnection(); // соединение с базой данных
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //для запросов
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //                                   обязат.        не обязат
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {// универсальный метод для обычных и параметризир, запросов
        if (params == null || params.length == 0) {
            return statement.executeQuery(query); // для обычных запросов
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(query);// для параметризиров.запросов
            for (int i = 0; i < params.length; i++){
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeQuery();
        }
    }


    public static boolean insertInto(String tableName, String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Number of columns and values must match.");
        }

        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            columnNames.append(columns[i]);
            placeholders.append("?");
            if (i < columns.length - 1) {
                columnNames.append(", ");
                placeholders.append(", ");
            }
        }

        String query = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columnNames, placeholders);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public static void closeConnection() throws SQLException {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
