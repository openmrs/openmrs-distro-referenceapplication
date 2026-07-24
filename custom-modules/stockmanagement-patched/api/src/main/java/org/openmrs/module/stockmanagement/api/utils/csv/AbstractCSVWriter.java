package org.openmrs.module.stockmanagement.api.utils.csv;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * The AbstractCSVWriter was created to prevent duplication of code between the CSVWriter and the
 * CSVParserWriter classes.
 * 
 * @since 4.2
 */
public abstract class AbstractCSVWriter {
	
	protected final Writer writer;
	
	protected String lineEnd;
	
	protected volatile IOException exception;
	
	/**
	 * Default line terminator.
	 */
	public static final String DEFAULT_LINE_END = "\n";
	
	/**
	 * RFC 4180 compliant line terminator.
	 */
	public static final String RFC4180_LINE_END = "\r\n";
	
	/**
	 * Default buffer sizes
	 */
	public static final int INITIAL_STRING_SIZE = 1024;
	
	/**
	 * The character used for escaping quotes.
	 */
	public static final char DEFAULT_ESCAPE_CHARACTER = '"';
	
	/**
	 * The default separator to use if none is supplied to the constructor.
	 */
	public static final char DEFAULT_SEPARATOR = ',';
	
	/**
	 * The default quote character to use if none is supplied to the constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';
	
	/**
	 * The quote constant to use when you wish to suppress all quoting.
	 */
	public static final char NO_QUOTE_CHARACTER = '\u0000';
	
	/**
	 * The escape constant to use when you wish to suppress all escaping.
	 */
	public static final char NO_ESCAPE_CHARACTER = '\u0000';
	
	/**
	 * Constructor to initialize the common values.
	 * 
	 * @param writer Writer used for output of csv data.
	 * @param lineEnd String to append at end of data (either "\n" or "\r\n").
	 */
	protected AbstractCSVWriter(Writer writer, String lineEnd) {
		this.writer = writer;
		this.lineEnd = lineEnd;
	}
	
	public void writeAll(Iterable<String[]> allLines, boolean applyQuotesToAll) {
		StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
		try {
			for (String[] line : allLines) {
				writeNext(line, applyQuotesToAll, sb);
				sb.setLength(0);
			}
		}
		catch (IOException e) {
			exception = e;
		}
	}
	
	public void writeNext(String[] nextLine, boolean applyQuotesToAll) {
		try {
			writeNext(nextLine, applyQuotesToAll, new StringBuilder(INITIAL_STRING_SIZE));
		}
		catch (IOException e) {
			exception = e;
		}
	}
	
	/**
	 * Writes the next line to the file. This method is a fail-fast method that will throw the
	 * IOException of the writer supplied to the CSVWriter (if the Writer does not handle the
	 * exceptions itself like the PrintWriter class).
	 * 
	 * @param nextLine a string array with each comma-separated element as a separate entry.
	 * @param applyQuotesToAll true if all values are to be quoted. false applies quotes only to
	 *            values which contain the separator, escape, quote or new line characters.
	 * @param appendable Appendable used as buffer.
	 * @throws IOException Exceptions thrown by the writer supplied to CSVWriter.
	 */
	protected abstract void writeNext(String[] nextLine, boolean applyQuotesToAll, Appendable appendable) throws IOException;
	
	public void flush() throws IOException {
		writer.flush();
	}
	
	public void close() throws IOException {
		flush();
		writer.close();
	}
	
	public boolean checkError() {
		
		if (writer instanceof PrintWriter) {
			PrintWriter pw = (PrintWriter) writer;
			return pw.checkError();
		}
		if (exception != null) { // we don't want to lose the original exception
			flushQuietly(); // checkError in the PrintWriter class flushes the buffer so we shall too.
		} else {
			try {
				flush();
			}
			catch (IOException ioe) {
				exception = ioe;
			}
		}
		return exception != null;
	}
	
	public IOException getException() {
		return exception;
	}
	
	public void resetError() {
		exception = null;
	}
	
	void flushQuietly() {
		try {
			flush();
		}
		catch (IOException e) {
			// catch exception and ignore.
		}
	}
	
}
