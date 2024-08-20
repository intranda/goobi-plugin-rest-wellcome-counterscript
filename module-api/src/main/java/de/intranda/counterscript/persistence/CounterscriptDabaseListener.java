package de.intranda.counterscript.persistence;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.sub.goobi.persistence.managers.DatabaseVersion;

@WebListener
public class CounterscriptDabaseListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * Method runs during startup
     */

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // create table counterscript_files, if the table doesn't exist
        if (!DatabaseVersion.checkIfTableExists("counterscript_files")) {
            StringBuilder createTable = new StringBuilder();
            createTable.append("CREATE TABLE counterscript_files ( ");
            createTable.append("id int(10) unsigned NOT NULL AUTO_INCREMENT, ");
            createTable.append("filename varchar(255) DEFAULT NULL, ");
            createTable.append("bnumber varchar(255) DEFAULT NULL, ");
            createTable.append("material varchar(255) DEFAULT NULL, ");
            createTable.append("access_status varchar(255) DEFAULT NULL, ");
            createTable.append("access_licence varchar(255) DEFAULT NULL, ");
            createTable.append("player_permission varchar(255) DEFAULT NULL, ");
            createTable.append("status varchar(255) DEFAULT NULL, ");
            createTable.append("creation_date datetime DEFAULT NULL, ");
            createTable.append("modification_date datetime DEFAULT NULL, ");
            createTable.append("deletion_date datetime DEFAULT NULL, ");
            createTable.append("current tinyint DEFAULT '0', ");
            createTable.append("PRIMARY KEY (id) ");
            createTable.append(") ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 ");
            try {
                DatabaseVersion.runSql(createTable.toString());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
