package com.streamify.backend;
// jordan added ts
import java.sql.*;

//test file for raw jdbc
public class RawJDBCTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/streamify_db",
                    "streamify_user",
                    "streamify_pass"
            );

            System.out.println("Connected to database");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT content_name FROM content");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

