package src.afsql;

import java.sql.ResultSetMetaData;

/**
 * 列信息保存类
 */
public class AfSqlMeta {
    public int index;
    public String label;
    public int type;		 // java.sql.Types
    public String typeName;

	/**
	 * 读取每一列的 MetaData(元数据信息)
	 * @param rsmd
	 * @return 元数据信息数组
	 * @throws Exception
	 */
    public static AfSqlMeta[] read(ResultSetMetaData rsmd) throws Exception {
        int numColumn = rsmd.getColumnCount();
        AfSqlMeta[] result = new AfSqlMeta[numColumn];
        for (int i = 0; i < numColumn; i++) {
            AfSqlMeta m = result[i] = new AfSqlMeta();
            int column = i + 1;
            m.index = column;	// 序号
            m.label = rsmd.getColumnLabel(column);		// 列标题(别名)
            m.type = rsmd.getColumnType(column);		// 数据类型(参考java.sql.Types定义)
            m.typeName = rsmd.getColumnName(column);	// 列名
        }
        return result;
    }
}
