package com.soho.pos.utils;

public class ExceptionUtils {

	/**
	 * 將Exception內容轉成字串
	 * 
	 * @param e
	 * @return
	 */
	public static String toString(Exception e) {
		String tmp = e.getClass().getName();
		tmp = tmp + "\n" + e.getMessage();
		tmp = tmp + "\n" + getStackTrace(e.getStackTrace());
		return tmp;
	}

	/**
	 * 請改成呼叫toString
	 * 
	 * @param ste
	 * @return
	 */
	public static String getStackTrace(StackTraceElement[] ste) {
		StringBuilder s = new StringBuilder();
		for (StackTraceElement e : ste) {
			s.append(e).append("\n");
		}
		return s.toString();
	}

}
