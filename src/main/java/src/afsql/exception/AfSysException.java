package src.afsql.exception;

/**
 * 发生系统级错误抛出的异常
 */
public class AfSysException extends RuntimeException {

	private static final long serialVersionUID = 6806129545290130142L;

	/**
	 * 获得一个异常AfSysException
	 * @param message 异常描述
	 * @param cause 异常原因
	 */
	public AfSysException(String message, Throwable cause) {
		super(message, cause);
	}

}
