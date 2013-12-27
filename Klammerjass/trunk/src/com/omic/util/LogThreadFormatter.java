package com.omic.util;

/**
 * More readable LogFormatter for java.util.logging </br></br>
 * 
 * @author arbeitm <br/>
 * @version $Id: LogThreadFormatter.java,v 1.2 2008/10/31 10:00:33 p013519 Exp $
 *          <br/>
 * @see LogFormatter <br/>
 * 
 *      <b>(C) 2008 Dresdner Kleinwort - Juliet2</b>
 * 
 */
public class LogThreadFormatter extends LogFormatter {
	@Override
	protected String getThreadContext() {
		return Thread.currentThread().getName();
	}
}
