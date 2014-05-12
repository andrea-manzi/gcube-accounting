package org.gcube.messaging.common.messages.util;

import java.io.Serializable;
import java.sql.Types;

public class SQLType implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final SQLType ARRAY = new SQLType("ARRAY", Types.ARRAY);
    public static final SQLType BIGINT = new SQLType("BIGINT", Types.BIGINT);
    public static final SQLType BINARY = new SQLType("BINARY", Types.BINARY);
    public static final SQLType BIT = new SQLType("BIT", Types.BIT);
    public static final SQLType BLOB = new SQLType("BLOB", Types.BLOB);
    public static final SQLType BOOLEAN = new SQLType("BOOLEAN", Types.BOOLEAN);
    public static final SQLType CHAR = new SQLType("BLOB", Types.CHAR);
    public static final SQLType CLOB = new SQLType("CLOB", Types.CLOB);
    public static final SQLType DATALINK = new SQLType("DATALINK", Types.DATALINK);
    public static final SQLType DATE = new SQLType("DATE", Types.DATE);
    public static final SQLType DECIMAL = new SQLType("DECIMAL", Types.DECIMAL);
    public static final SQLType DISTINCT = new SQLType("DECIMAL", Types.DISTINCT);
    public static final SQLType DOUBLE = new SQLType("DOUBLE", Types.DOUBLE);
    public static final SQLType FLOAT = new SQLType("FLOAT", Types.FLOAT);
    public static final SQLType INTEGER = new SQLType("INTEGER", Types.INTEGER);
    public static final SQLType JAVA_OBJECT = new SQLType("INTEGER", Types.JAVA_OBJECT);
    public static final SQLType LONGVARBINARY = new SQLType("LONGVARBINARY", Types.LONGVARBINARY);
    public static final SQLType LONGVARCHAR = new SQLType("LONGVARBINARY", Types.LONGVARCHAR);
    public static final SQLType NULL = new SQLType("NULL", Types.NULL);
    public static final SQLType NUMERIC = new SQLType("NUMERIC", Types.NUMERIC);
    public static final SQLType OTHER = new SQLType("OTHER", Types.OTHER);
    public static final SQLType REAL = new SQLType("REAL", Types.REAL);
    public static final SQLType REF = new SQLType("REF", Types.REF);
    public static final SQLType SMALLINT = new SQLType("SMALLINT", Types.SMALLINT);
    public static final SQLType STRUCT = new SQLType("STRUCT", Types.STRUCT);
    public static final SQLType TIME = new SQLType("TIME", Types.TIME);
    public static final SQLType TIMESTAMP = new SQLType("TIMESTAMP", Types.TIMESTAMP);
    public static final SQLType TINYINT = new SQLType("TINYINT", Types.TINYINT);
    public static final SQLType VARBINARY = new SQLType("VARBINARY", Types.VARBINARY);
    public static final SQLType VARCHAR = new SQLType("VARCHAR", Types.VARCHAR);

    private final String myName;
    private int jdbcSQLType;

    private SQLType(String name, int jdbcSQLTypeValue) {
        myName = name;
        this.jdbcSQLType = jdbcSQLTypeValue;
    }

    public static SQLType getSQLType (Object object){
    	if (object instanceof java.lang.String)
    		return VARCHAR;
    	if (object instanceof java.math.BigDecimal)
    		return NUMERIC;
    	if (object instanceof java.lang.Boolean )
    		return BIT;
    	if (object instanceof java.lang.Integer)
    		return INTEGER;
    	if (object instanceof java.lang.Long)
    		return BIGINT;
    	if (object instanceof java.lang.Float)
    		return REAL;
    	if (object instanceof java.lang.Double)
    		return DOUBLE;
    	if (object instanceof byte[])
    		return LONGVARBINARY;
    	if (object instanceof java.sql.Date)
    		return DATE;
    	if (object instanceof java.sql.Time)
    		return TIME;
    	if (object instanceof java.sql.Timestamp)
    		return TIMESTAMP;
    	if (object instanceof java.sql.Clob)
    		return CLOB;
    	if (object instanceof java.sql.Blob)
    		return BLOB;
    	if (object instanceof java.sql.Array)
    		return ARRAY;
    	if (object instanceof java.sql.Struct)
    		return STRUCT;
    	if (object instanceof java.sql.Ref)
    		return 	REF;
        throw new IllegalArgumentException("Unsupported JAVA Type: " + object.getClass().getCanonicalName());
    	
    }
    
    public static SQLType getSQLType(int jdbcSQLType) {
        switch (jdbcSQLType) {
            case Types.ARRAY:
                return ARRAY;
            case Types.BIGINT:
                return BIGINT;
            case Types.BINARY:
                return BINARY;
            case Types.BIT:
                return BIT;
            case Types.BLOB:
                return BLOB;
            case Types.BOOLEAN:
                return BOOLEAN;
            case Types.CHAR:
                return CHAR;
            case Types.CLOB:
                return CLOB;
            case Types.DATALINK:
                return DATALINK;
            case Types.DATE:
                return DATE;
            case Types.DECIMAL:
                return DECIMAL;
            case Types.DOUBLE:
                return DOUBLE;
            case Types.DISTINCT:
                return DISTINCT;
            case Types.FLOAT:
                return FLOAT;
            case Types.INTEGER:
                return INTEGER;
            case Types.JAVA_OBJECT:
                return JAVA_OBJECT;
            case Types.LONGVARBINARY:
                return LONGVARBINARY;
            case Types.LONGVARCHAR:
                return LONGVARCHAR;
            case Types.NULL:
                return NULL;
            case Types.NUMERIC:
                return NUMERIC;
            case Types.OTHER:
                return OTHER;
            case Types.REAL:
                return REAL;
            case Types.REF:
                return REF;
            case Types.SMALLINT:
                return SMALLINT;
            case Types.STRUCT:
                return STRUCT;
            case Types.TIME:
                return TIME;
            case Types.TIMESTAMP:
                return TIMESTAMP;
            case Types.TINYINT:
                return TINYINT;
            case Types.VARBINARY:
                return VARBINARY;
            case Types.VARCHAR:
                return VARCHAR;
            default:
                throw new IllegalArgumentException("Unricognized JDBC SQL Type: " + jdbcSQLType + " Please refer to java.sql.Types of J2SE API");
        }
    }

    public String toString() {
        return "SQL type: " + myName + " JDBC SQL type: " + jdbcSQLType;
    }
    
    public String getName() {
        return myName;
    }

    public int getJDBCSQLType() {
        return jdbcSQLType;
    }
}