package com.omic.util;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * More readable LogFormatter for java.util.logging </br></br> SAMPLE: </br> 1)
 * Add logging.properties to your classpath with content: </br> <code>
 *    java.util.logging.ConsoleHandler.level=ALL </br>
 *    java.util.logging.ConsoleHandler.formatter=com.dkib.juliet2.log.LogFormatter </br>
 *    MyServer.handlers=java.util.logging.ConsoleHandler </br>
 *    MyServer.level=ALL </br>
 * </code> </br> 2) Setup java.logging: </br> <code>
 *   	InputStream is = ClassLoader.getSystemResourceAsStream ("logging.properties"); </br>
 * 		LogManager.getLogManager().readConfiguration (is); </br>
 * 		is.close(); </br>
 * 		.... </br>
 *    log = Logger.getLogger("OrionServer"); </br>
 *     </br>
 *   <b>OR</b>
 *    </br> 
 *    set JRE property -Djava.util.logging.config.file=classes/logging.properties  </br>
 * </code>
 * 
 * @author arbeitm <br>
 * @version $Id: LogFormatter.java,v 1.8 2009/02/09 12:41:49 p011427 Exp $ <br>
 * 
 *          <b>(C) 2008 Dresdner Kleinwort - Juliet2</b>
 * 
 */
public class LogFormatter extends Formatter {
	Date dat = new Date();
	private SimpleDateFormat formatter = null;
	private String lineSeparator = "\n"; // (String)
											// java.security.AccessController
											// .doPrivileged(new
											// sun.security.action
											// .GetPropertyAction
											// ("line.separator"));

	/**
	 * Format the given LogRecord.
	 * 
	 * @param record
	 *            the log record to be formatted.
	 * @return a formatted log record
	 */
	@Override
	public synchronized String format(LogRecord record) {
		boolean isInfo = record.getLevel().intValue()==Level.INFO.intValue();
		
		StringBuffer sb = new StringBuffer();
		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		StringBuffer text = new StringBuffer();
		if (formatter == null) {
			formatter = new SimpleDateFormat("dd.MM.yy-HH:mm:ss");
		}
		formatter.format(dat, text, new FieldPosition(0));
		sb.append(text);
		if (getThreadContext() != null) {
			sb.append(" [");
			sb.append(getThreadContext());
			sb.append("]");
		}
		if (getRMIContext() != null) {
			sb.append(" [");
			sb.append(getRMIContext());
			sb.append("]");
		}
		sb.append(" [");
		sb.append(record.getLevel().getLocalizedName());
		sb.append("] ");
		
		if (!isInfo) {
			if (record.getSourceClassName() != null) {
				String className = record.getSourceClassName();
				try {
					sb.append(className.substring(className.lastIndexOf('.')+1));
				} catch (StringIndexOutOfBoundsException e) {
					sb.append(className);
				}
			}
			else {
				sb.append(record.getLoggerName());
			}
			if (record.getSourceMethodName() != null) {
				sb.append(".");
				sb.append(record.getSourceMethodName());
			}
			sb.append("() - ");
		}
		
		String message = formatMessage(record);
		sb.append(message);
		sb.append(lineSeparator);
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			}
			catch (Exception ex) {
			}
		}
		return sb.toString();
	}

	/**
	 * @return Client IP in case of RMI call
	 * @see http://forums.sun.com/thread.jspa?threadID=734417&messageID=4221743
	 */
	protected String getRMIContext() {
		return null;
	}

	/**
	 * @return Current thread ID
	 */
	protected String getThreadContext() {
		return null;
	}
}
