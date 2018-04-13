package com.uzak.simjdbc.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NullPrimaryKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5118496559231937725L;

	public NullPrimaryKeyException() {
		super();
	}

	public NullPrimaryKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NullPrimaryKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullPrimaryKeyException(String message) {
		super(message);
	}

	public NullPrimaryKeyException(Throwable cause) {
		super(cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return super.fillInStackTrace();
	}

	@Override
	public synchronized Throwable getCause() {
		return super.getCause();
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}

	@Override
	public synchronized Throwable initCause(Throwable arg0) {
		return super.initCause(arg0);
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream arg0) {
		super.printStackTrace(arg0);
	}

	@Override
	public void printStackTrace(PrintWriter arg0) {
		super.printStackTrace(arg0);
	}

	@Override
	public void setStackTrace(StackTraceElement[] arg0) {
		super.setStackTrace(arg0);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
