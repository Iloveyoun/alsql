package src.afsql.exception;

/**
 * 在执行SQL时抛出的异常
 */
public class AfSqlException extends RuntimeException {

	private static final long serialVersionUID = 6806129545290130142L;

	/**
	 * 获得一个异常AfSQLException
	 * 
	 * @param message 异常描述
	 * @param cause 异常原因
	 */
	public AfSqlException(String message, Throwable cause) {
		super(message, cause);
	}

}
