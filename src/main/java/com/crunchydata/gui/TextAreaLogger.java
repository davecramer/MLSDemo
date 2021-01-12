package com.crunchydata.gui;

import javax.swing.*;
import java.util.logging.*;

public class TextAreaLogger extends Handler {

    final JTextArea logText;

    public TextAreaLogger(JTextArea logText) {
        this.logText = logText;
    }

    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * The <tt>Handler</tt>  is responsible for formatting the message, when and
     * if necessary.  The formatting should include localization.
     *
     * @param record description of the log event. A null record is
     *               silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        String msg;
        try {
            Formatter formatter = getFormatter();
            if (formatter == null ) {
                formatter = new SimpleFormatter();
            }
            msg = formatter.format(record);
        } catch (Exception ex) {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }
        logText.append(msg);
    }

    /**
     * Flush any buffered output.
     */
    @Override
    public void flush() {

    }

    /**
     * Close the <tt>Handler</tt> and free all associated resources.
     * <p>
     * The close method will perform a <tt>flush</tt> and then close the
     * <tt>Handler</tt>.   After close has been called this <tt>Handler</tt>
     * should no longer be used.  Method calls may either be silently
     * ignored or may throw runtime exceptions.
     *
     * @throws SecurityException if a security manager exists and if
     *                           the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    @Override
    public void close() throws SecurityException {

    }
}
