package my.englsih.exception;

public class MyException extends Exception {

	public MyException(String msg) {
		super(msg);
	}

	public MyException(String msg, Throwable ex) {
		super(msg, ex);
	}

	public MyException(Throwable ex) {
		super(ex);
	}
}
