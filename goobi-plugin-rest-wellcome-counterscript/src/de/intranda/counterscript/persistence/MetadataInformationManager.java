package de.intranda.counterscript.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import de.intranda.counterscript.model.MetadataInformation;
import de.intranda.counterscript.model.MetadataInformation.StatusType;
import de.sub.goobi.persistence.managers.MySQLHelper;
import lombok.extern.log4j.Log4j;

@Log4j
public class MetadataInformationManager {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_FILENAME = "filename";
    private static final String COLUMN_NAME_BNUMBER = "bnumber";
    private static final String COLUMN_NAME_MATERIAL = "material";
    private static final String COLUMN_NAME_ACCESS_STATUS = "access_status";
    private static final String COLUMN_NAME_ACCESS_LICENCE = "access_licence";
    private static final String COLUMN_NAME_PLAYER_PERMISSION = "player_permission";
    private static final String COLUMN_NAME_STATUS = "status";
    private static final String COLUMN_NAME_DATE_CREATION = "creation_date";
    private static final String COLUMN_NAME_DATE_MODIFICATION = "modification_date";
    private static final String COLUMN_NAME_DATE_DELETION = "deletion_date";
    private static final String COLUMN_NAME_CURRENT = "current";

    public static MetadataInformation searchForActiveData(String filename) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM counterscript_files WHERE ");
        sql.append(COLUMN_NAME_CURRENT);
        sql.append(" = true AND ");
        sql.append(COLUMN_NAME_STATUS);
        sql.append(" != '");
        sql.append(StatusType.deleted);
        sql.append("' AND ");
        sql.append(COLUMN_NAME_FILENAME);
        sql.append(" = '");
        sql.append(filename + "'");

        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }

    }

    public static void saveMetadataInformation(MetadataInformation mi) throws SQLException {
        if (mi.getId() == null) {
            // new entry, insert everything
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO counterscript_files (");
            sb.append(COLUMN_NAME_FILENAME);
            sb.append(", ");
            sb.append(COLUMN_NAME_BNUMBER);
            sb.append(", ");
            sb.append(COLUMN_NAME_MATERIAL);
            sb.append(", ");
            sb.append(COLUMN_NAME_ACCESS_STATUS);
            sb.append(", ");
            sb.append(COLUMN_NAME_ACCESS_LICENCE);
            sb.append(", ");
            sb.append(COLUMN_NAME_PLAYER_PERMISSION);
            sb.append(", ");
            sb.append(COLUMN_NAME_STATUS);
            sb.append(", ");
            sb.append(COLUMN_NAME_DATE_CREATION);
            sb.append(", ");
            sb.append(COLUMN_NAME_DATE_MODIFICATION);
            sb.append(", ");
            sb.append(COLUMN_NAME_DATE_DELETION);
            sb.append(", ");
            sb.append(COLUMN_NAME_CURRENT);
            sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            Object[] parameter =
                { mi.getFilename(), mi.getBnumber(), mi.getMaterial(), mi.getAccessStatus(), mi.getAccessLicence(), mi.getPlayerPermissions(),
                        mi.getStatus().toString(), mi.getCreationDate() == null ? null : new Timestamp(mi.getCreationDate().getTime()),
                                mi.getModificationDate() == null ? null : new Timestamp(mi.getModificationDate().getTime()),
                                        mi.getDeletionDate() == null ? null : new Timestamp(mi.getDeletionDate().getTime()), mi.isActive() };
            Connection connection = null;
            try {
                connection = MySQLHelper.getInstance().getConnection();
                QueryRunner run = new QueryRunner();
                //                if (log.isDebugEnabled()) {
                //                    log.debug(sb.toString() + ", " + Arrays.toString(parameter));
                //                }
                Integer id = run.insert(connection, sb.toString(), MySQLHelper.resultSetToIntegerHandler, parameter);
                if (id != null) {
                    mi.setId(id);
                }
            } finally {
                if (connection != null) {
                    MySQLHelper.closeConnection(connection);
                }
            }

        } else {
            // update old entry (current = false)
            String query = "UPDATE counterscript_files SET current = false WHERE id = ?";
            Connection connection = null;
            try {
                connection = MySQLHelper.getInstance().getConnection();
                QueryRunner run = new QueryRunner();
                run.update(connection, query, mi.getId());

            } finally {
                if (connection != null) {
                    MySQLHelper.closeConnection(connection);
                }
            }
        }

    }

    private static ResultSetHandler<MetadataInformation> resultSetToMetadataInformationHandler = new ResultSetHandler<MetadataInformation>() {
        @Override
        public MetadataInformation handle(ResultSet rs) throws SQLException {
            try {
                if (rs.next()) {
                    return convertMetadataInformation(rs);
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
            return null;
        }
    };

    private static ResultSetHandler<List<MetadataInformation>> resultSetToMetadataInformationListHandler =
            new ResultSetHandler<List<MetadataInformation>>() {
        @Override
        public List<MetadataInformation> handle(ResultSet rs) throws SQLException {
            try {
                List<MetadataInformation> metadataList = new ArrayList<>();
                while (rs.next()) {
                    metadataList.add(convertMetadataInformation(rs));
                }
                return metadataList;
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        }
    };

    public static MetadataInformation convertMetadataInformation(ResultSet rs) throws SQLException {
        Integer id = rs.getInt(COLUMN_NAME_ID);
        if (rs.wasNull()) {
            id = null;
        }
        MetadataInformation mi = new MetadataInformation();
        mi.setId(id);
        mi.setFilename(rs.getString(COLUMN_NAME_FILENAME));
        mi.setBnumber(rs.getString(COLUMN_NAME_BNUMBER));
        mi.setMaterial(rs.getString(COLUMN_NAME_MATERIAL));
        mi.setAccessStatus(rs.getString(COLUMN_NAME_ACCESS_STATUS));
        mi.setAccessLicence(rs.getString(COLUMN_NAME_ACCESS_LICENCE));
        mi.setPlayerPermissions(rs.getString(COLUMN_NAME_PLAYER_PERMISSION));

        Timestamp creation = rs.getTimestamp(COLUMN_NAME_DATE_CREATION);
        Timestamp modification = rs.getTimestamp(COLUMN_NAME_DATE_MODIFICATION);
        Timestamp deletion = rs.getTimestamp(COLUMN_NAME_DATE_DELETION);

        if (creation != null) {
            mi.setCreationDate(new java.util.Date(creation.getTime()));
        }

        if (modification != null) {
            mi.setModificationDate(new java.util.Date(modification.getTime()));
        }

        if (deletion != null) {
            mi.setDeletionDate(new java.util.Date(deletion.getTime()));
        }

        mi.setStatus(StatusType.valueOf(rs.getString(COLUMN_NAME_STATUS)));

        mi.setActive(rs.getBoolean(COLUMN_NAME_CURRENT));

        return mi;
    }

    public static List<MetadataInformation> getAllElementsInFolder(String folder) throws SQLException {
        String sql = "SELECT * FROM counterscript_files WHERE filename LIKE '" + folder + "%' AND current = true AND status != 'deleted';";

        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationListHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }
    }

    public static MetadataInformation searchForDeletedData(String filename) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM counterscript_files WHERE ");
        sql.append(COLUMN_NAME_CURRENT);
        sql.append(" = true AND ");
        sql.append(COLUMN_NAME_STATUS);
        sql.append(" = '");
        sql.append(StatusType.deleted);
        sql.append("' AND ");
        sql.append(COLUMN_NAME_FILENAME);
        sql.append(" = '");
        sql.append(filename + "'");
        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }
    }

    public static List<MetadataInformation> calculateDataDate(Date starttime, Date endtime, boolean includeInactiveData) throws SQLException {
        StringBuilder sql = new StringBuilder();
        boolean whereAppended = false;
        sql.append("SELECT * FROM counterscript_files ");
        if (!includeInactiveData) {
            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" current = true ");

        }
        if (starttime != null) {
            Timestamp start = new Timestamp(starttime.getTime());

            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" (creation_date > '");
            sql.append(start);
            sql.append("' OR modification_date > '");
            sql.append(start);
            sql.append("' OR deletion_date > '");
            sql.append(start);
            sql.append("') ");

        }

        if (endtime != null) {
            Timestamp end = new Timestamp(endtime.getTime());

            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" (creation_date < '");
            sql.append(end);
            sql.append(" ' OR modification_date < '");
            sql.append(end);
            sql.append(" ' OR deletion_date < '");
            sql.append(end);
            sql.append("') ");

        }

        log.debug(sql.toString());
        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationListHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }
    }

    public static List<MetadataInformation> calculateDataForIdentifier(String number) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM counterscript_files WHERE ");
        sql.append(COLUMN_NAME_BNUMBER);
        sql.append(" = '");
        sql.append(number);
        sql.append("';");

        log.debug(sql.toString());
        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationListHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }
    }

    public static List<MetadataInformation> calculateDataString(String start, String endtime, boolean includeInactiveData) throws SQLException {
        StringBuilder sql = new StringBuilder();
        boolean whereAppended = false;
        sql.append("SELECT * FROM counterscript_files ");
        if (!includeInactiveData) {
            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" current = true ");

        }
        if (start != null) {

            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" (creation_date > '");
            sql.append(start);
            sql.append("' OR modification_date > '");
            sql.append(start);
            sql.append("' OR deletion_date > '");
            sql.append(start);
            sql.append("') ");

        }

        if (endtime != null) {

            if (!whereAppended) {
                sql.append("WHERE ");
                whereAppended = true;
            } else {
                sql.append("AND ");
            }
            sql.append(" (creation_date < '");
            sql.append(endtime);
            sql.append(" ' OR modification_date < '");
            sql.append(endtime);
            sql.append(" ' OR deletion_date < '");
            sql.append(endtime);
            sql.append("') ");

        }

        log.debug(sql.toString());
        Connection connection = null;
        try {
            connection = MySQLHelper.getInstance().getConnection();
            QueryRunner run = new QueryRunner();

            return run.query(connection, sql.toString(), resultSetToMetadataInformationListHandler);

        } finally {
            if (connection != null) {
                MySQLHelper.closeConnection(connection);
            }
        }
    }

}
