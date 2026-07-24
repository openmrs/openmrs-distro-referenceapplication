package org.openmrs.module.stockmanagement.api.utils.csv;

/*
 Copyright 2015 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.io.IOException;
import java.io.Writer;

/**
 * A very simple CSV writer released under a commercial-friendly license.
 * 
 * @author Glen Smith
 */
public class CSVWriter extends AbstractCSVWriter {
	
	protected final char separator;
	
	protected final char quotechar;
	
	protected final char escapechar;
	
	/**
	 * Constructs CSVWriter using a comma for the separator.
	 * 
	 * @param writer The writer to an underlying CSV source.
	 */
	public CSVWriter(Writer writer) {
		this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
	}
	
	/**
	 * Constructs CSVWriter with supplied separator, quote char, escape char and line ending.
	 * 
	 * @param writer The writer to an underlying CSV source.
	 * @param separator The delimiter to use for separating entries
	 * @param quotechar The character to use for quoted elements
	 * @param escapechar The character to use for escaping quotechars or escapechars
	 * @param lineEnd The line feed terminator to use
	 */
	public CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
		super(writer, lineEnd);
		this.escapechar = escapechar;
		this.quotechar = quotechar;
		this.separator = separator;
	}
	
	@Override
	protected void writeNext(String[] nextLine, boolean applyQuotesToAll, Appendable appendable) throws IOException {
		if (nextLine == null) {
			return;
		}
		
		for (int i = 0; i < nextLine.length; i++) {
			
			if (i != 0) {
				appendable.append(separator);
			}
			
			String nextElement = nextLine[i];
			
			if (nextElement == null) {
				continue;
			}
			
			Boolean stringContainsSpecialCharacters = stringContainsSpecialCharacters(nextElement);
			
			appendQuoteCharacterIfNeeded(applyQuotesToAll, appendable, stringContainsSpecialCharacters);
			
			if (stringContainsSpecialCharacters) {
				processLine(nextElement, appendable);
			} else {
				appendable.append(nextElement);
			}
			
			appendQuoteCharacterIfNeeded(applyQuotesToAll, appendable, stringContainsSpecialCharacters);
		}
		
		appendable.append(lineEnd);
		writer.write(appendable.toString());
	}
	
	private void appendQuoteCharacterIfNeeded(boolean applyQuotesToAll, Appendable appendable,
	        Boolean stringContainsSpecialCharacters) throws IOException {
		if ((applyQuotesToAll || stringContainsSpecialCharacters) && quotechar != NO_QUOTE_CHARACTER) {
			appendable.append(quotechar);
		}
	}
	
	/**
	 * Checks to see if the line contains special characters.
	 * 
	 * @param line Element of data to check for special characters.
	 * @return True if the line contains the quote, escape, separator, newline, or return.
	 */
	protected boolean stringContainsSpecialCharacters(String line) {
		return line.indexOf(quotechar) != -1 || line.indexOf(escapechar) != -1 || line.indexOf(separator) != -1
		        || line.contains(DEFAULT_LINE_END) || line.contains("\r");
	}
	
	/**
	 * Processes all the characters in a line.
	 * 
	 * @param nextElement Element to process.
	 * @param appendable - Appendable holding the processed data.
	 * @throws IOException - IOException thrown by the writer supplied to the CSVWriter
	 */
	protected void processLine(String nextElement, Appendable appendable) throws IOException {
		for (int j = 0; j < nextElement.length(); j++) {
			char nextChar = nextElement.charAt(j);
			processCharacter(appendable, nextChar);
		}
	}
	
	/**
	 * Appends the character to the StringBuilder adding the escape character if needed.
	 * 
	 * @param appendable - Appendable holding the processed data.
	 * @param nextChar Character to process
	 * @throws IOException - IOException thrown by the writer supplied to the CSVWriter.
	 */
	protected void processCharacter(Appendable appendable, char nextChar) throws IOException {
		if (escapechar != NO_ESCAPE_CHARACTER && checkCharactersToEscape(nextChar)) {
			appendable.append(escapechar);
		}
		appendable.append(nextChar);
	}
	
	/**
	 * Checks whether the next character that is to be written out is a special character that must
	 * be quoted. The quote character, escape charater, and separator are special characters.
	 * 
	 * @param nextChar The next character to be written
	 * @return Whether the character needs to be quoted or not
	 */
	protected boolean checkCharactersToEscape(char nextChar) {
		return quotechar == NO_QUOTE_CHARACTER ? (nextChar == quotechar || nextChar == escapechar || nextChar == separator || nextChar == '\n')
		        : (nextChar == quotechar || nextChar == escapechar);
	}
	
}
