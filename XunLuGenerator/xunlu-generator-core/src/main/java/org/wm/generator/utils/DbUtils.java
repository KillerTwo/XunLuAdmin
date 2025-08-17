package org.wm.generator.utils;

import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.wm.generator.config.DbType;
import org.wm.generator.config.GenDataSource;
import oracle.jdbc.OracleConnection;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB工具类
 *
 * @author eumenides
 * 
 */
public class DbUtils {

    private final static String AES_KEY = "makulowcodeyyds1";

    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    /**
     * 获得数据库连接
     */
    public static Connection getConnection(GenDataSource dataSource) throws ClassNotFoundException, SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        Class.forName(dataSource.getDbType().getDriverClass());
        var passwordEnc = dataSource.getPassword();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES,
                AES_KEY.getBytes(StandardCharsets.UTF_8));

        var password = aes.decryptStr(passwordEnc);

        Connection connection = DriverManager.getConnection(dataSource.getConnUrl(), dataSource.getUsername(), password);
        if (dataSource.getDbType() == DbType.Oracle) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }

        return connection;
    }


}