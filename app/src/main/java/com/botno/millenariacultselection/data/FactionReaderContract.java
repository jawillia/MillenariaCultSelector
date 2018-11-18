package com.botno.millenariacultselection.data;

import android.provider.BaseColumns;

/**
 * Created by User on 1/15/2015.
 */
public final class FactionReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FactionReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PremadeCult implements BaseColumns {
        public final static String CULT_TABLE_NAME = "cults";
        public static final String CULT_COLUMN_NAME = "cult_name";
        public static final String CULT_COLUMN_DIFFICULTY = "difficulty";
        public static final String CULT_COLUMN_EXPANSION = "expansion";
        public static final String CULT_COLUMN_ICON = "icon";
        public static final String CULT_COLUMN_ACTIVE = "active";

        public static final String CREATE_TABLE = DBTYPE.CREATE_TABLE +
                CULT_TABLE_NAME + DBTYPE.PARENTHESIS_OPEN +
                _ID + DBTYPE.INTEGER_PRIMARY_KEY + DBTYPE.COMMA +
                CULT_COLUMN_NAME + DBTYPE.TEXT + DBTYPE.NOT_NULL + DBTYPE.COMMA +
                CULT_COLUMN_DIFFICULTY + DBTYPE.TEXT + DBTYPE.COMMA +
                CULT_COLUMN_EXPANSION + DBTYPE.TEXT + DBTYPE.COMMA +
                CULT_COLUMN_ICON + DBTYPE.BLOB + DBTYPE.COMMA +
                CULT_COLUMN_ACTIVE + DBTYPE.INTEGER + " DEFAULT 0" + DBTYPE.PARENTHESIS_CLOSE;

        public static final String DELETE_TABLE = DBTYPE.DROP_TABLE_IF_EXISTS + CULT_TABLE_NAME;
    }

    /**
     * DataBase type
     */
    public static abstract class DBTYPE {
        public static final String BLOB                     = " BLOB";
        public static final String INTEGER                     = " INTEGER";
        public static final String INTEGER_PRIMARY_KEY         = " INTEGER PRIMARY KEY";
        public static final String NULL                     = " NULL";
        public static final String NOT_NULL                 = " NOT NULL";
        public static final String REAL                     = " REAL";
        public static final String TEXT                     = " TEXT";
        public static final String COMMA                     = ", ";
        public static final String PARENTHESIS_OPEN         = " (";
        public static final String PARENTHESIS_CLOSE         = " );";
        public static final String CREATE_TABLE             = "CREATE TABLE ";
        public static final String DROP_TABLE_IF_EXISTS     = "DROP TABLE IF EXISTS ";
    }
}
