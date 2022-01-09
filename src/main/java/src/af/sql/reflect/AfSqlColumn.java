package src.af.sql.reflect;

/** 数据库列表字段的详细定义
 * 
 */
public class AfSqlColumn
{
	public String name;// 属性名, 须与列名相同
	public String type; // 映射成的Java类, 未使用
	public boolean primaryKey = false;// 未使用
	public int displaySize = 0; // 未使用
	public boolean isNotNull = false; // 未使用
	public String arg1; // 未使用
	public String arg2; // 未使用
	public String arg3;
	public String arg4;
}
