package src.afsql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AfSql
{
	// 数据库类型
	public static final int MYSQL = 1;
	public static final int ORACLE = 2;
	public static final int SQLSERVER = 3;
	public static final int DB2 = 4;
	
	public static DateFormat dateFormat()
	{
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public static DateFormat dateTimeFormat()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}	
	
}
