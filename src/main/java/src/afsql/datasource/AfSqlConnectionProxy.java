package src.afsql.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 动态代理Connection，以便在Connection调用close()时不是真正的关闭，而是将其重新放到连接池内
 */
class AfSqlConnectionProxy implements InvocationHandler {

	// 将要被代理的对象
	private Connection connection;
	// 所绑定的连接池
	private AfSqlDataSource dataSource;

	/**
	 * 构造一个代理对象
	 * @param connection 将要被代理的对象
	 */
	public AfSqlConnectionProxy(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 返回代理对象
	 * @return JDBC连接
	 */
	public Connection getConnection() {
		return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader()
										, new Class[] { Connection.class }
										, this);
	}

	// 代理其关闭方法
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("close")) {
			dataSource.callBackConn(connection); // 通知连接池回收此链接
			return null;
		}
		return method.invoke(connection, args); // 执行此底层方法
	}

	// 真正的关闭方法
	public void closeClose() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AfSqlConnectionProxy setAfSqlDataSource(AfSqlDataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

}
