/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.texlipse.spelling;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.IRegion;
import org.eclipse.osgi.util.TextProcessor;

/**
 * Helper class to provide String manipulation functions not available in standard JDK.
 */
// @see JDTUIHelperClasses
// @see org.eclipse.jdt.internal.corext.util.Strings (subclass of this one)
public class Strings {

	protected Strings(){}


	/**
	 * Tells whether we have to use the {@link TextProcessor}
	 * <p>
	 * This is used for performance optimization.
	 * </p>
	 * @since 3.4
	 */
	public static final boolean USE_TEXT_PROCESSOR;
	static {
		String testString= "args : String[]"; //$NON-NLS-1$
		USE_TEXT_PROCESSOR= testString != TextProcessor.process(testString);
	}

	protected static final String JAVA_ELEMENT_DELIMITERS= TextProcessor.getDefaultDelimiters() + "<>(),?{} "; //$NON-NLS-1$

	/**
	 * Adds special marks so that that the given string is readable in a BiDi environment.
	 *
	 * @param string the string
	 * @return the processed styled string
	 * @since 3.4
	 */
	public static String markLTR(String string) {
		if (!USE_TEXT_PROCESSOR)
			return string;

		return TextProcessor.process(string);
	}

	/**
	 * Adds special marks so that that the given string is readable in a BiDi environment.
	 *
	 * @param string the string
	 * @param delimiters the delimiters
	 * @return the processed styled string
	 * @since 3.4
	 */
	public static String markLTR(String string, String delimiters) {
		if (!USE_TEXT_PROCESSOR)
			return string;

		return TextProcessor.process(string, delimiters);
	}

	/**
	 * Adds special marks so that that the given Java element label is readable in a BiDi
	 * environment.
	 *
	 * @param string the string
	 * @return the processed styled string
	 * @since 3.6
	 */
	public static String markJavaElementLabelLTR(String string) {
		if (!USE_TEXT_PROCESSOR)
			return string;

		return TextProcessor.process(string, JAVA_ELEMENT_DELIMITERS);
	}

	/**
	 * Tests if a char is lower case. Fix for 26529.
	 * @param ch the char
	 * @return return true if char is lower case
	 */
	public static boolean isLowerCase(char ch) {
		return Character.toLowerCase(ch) == ch;
	}

	public static boolean startsWithIgnoreCase(String text, String prefix) {
		int textLength= text.length();
		int prefixLength= prefix.length();
		if (textLength < prefixLength)
			return false;
		for (int i= prefixLength - 1; i >= 0; i--) {
			if (Character.toLowerCase(prefix.charAt(i)) != Character.toLowerCase(text.charAt(i)))
				return false;
		}
		return true;
	}

	public static String removeNewLine(String message) {
		StringBuilder result= new StringBuilder();
		int current= 0;
		int index= message.indexOf('\n', 0);
		while (index != -1) {
			result.append(message.substring(current, index));
			if (current < index && index != 0)
				result.append(' ');
			current= index + 1;
			index= message.indexOf('\n', current);
		}
		result.append(message.substring(current));
		return result.toString();
	}

	/**
	 * Converts the given string into an array of lines. The lines
	 * don't contain any line delimiter characters.
	 *
	 * @param input the string
	 * @return the string converted into an array of strings. Returns <code>
	 * 	null</code> if the input string can't be converted in an array of lines.
	 */
	public static String[] convertIntoLines(String input) {
		try {
			ILineTracker tracker= new DefaultLineTracker();
			tracker.set(input);
			int size= tracker.getNumberOfLines();
			String result[]= new String[size];
			for (int i= 0; i < size; i++) {
				IRegion region= tracker.getLineInformation(i);
				int offset= region.getOffset();
				result[i]= input.substring(offset, offset + region.getLength());
			}
			return result;
		} catch (BadLocationException e) {
			return null;
		}
	}

	/**
	 * Returns <code>true</code> if the given string only consists of
	 * white spaces according to Java. If the string is empty, <code>true
	 * </code> is returned.
	 *
	 * @param s the string to test
	 * @return <code>true</code> if the string only consists of white
	 * 	spaces; otherwise <code>false</code> is returned
	 *
	 * @see java.lang.Character#isWhitespace(char)
	 */
	public static boolean containsOnlyWhitespaces(String s) {
		int size= s.length();
		for (int i= 0; i < size; i++) {
			if (!Character.isWhitespace(s.charAt(i)))
				return false;
		}
		return true;
	}






	public static String[] removeTrailingEmptyLines(String[] sourceLines) {
		int lastNonEmpty= findLastNonEmptyLineIndex(sourceLines);
		String[] result= new String[lastNonEmpty + 1];
		System.arraycopy(sourceLines, 0, result, 0, result.length);
		return result;
	}

	private static int findLastNonEmptyLineIndex(String[] sourceLines) {
		for (int i= sourceLines.length - 1; i >= 0; i--) {
			if (! sourceLines[i].trim().isEmpty())
				return i;
		}
		return -1;
	}

	/**
	 * Concatenate the given strings into one strings using the passed line delimiter as a
	 * delimiter. No delimiter is added to the last line.
	 * @param lines the lines
	 * @param delimiter line delimiter
	 * @return the concatenated lines
	 */
	public static String concatenate(String[] lines, String delimiter) {
		StringBuilder buffer= new StringBuilder();
		for (int i= 0; i < lines.length; i++) {
			if (i > 0)
				buffer.append(delimiter);
			buffer.append(lines[i]);
		}
		return buffer.toString();
	}

	public static boolean equals(String s, char[] c) {
		if (s.length() != c.length)
			return false;

		for (int i = c.length; --i >= 0;)
			if (s.charAt(i) != c[i])
				return false;
		return true;
	}

	public static String removeTrailingCharacters(String text, char toRemove) {
		int size= text.length();
		int end= size;
		for (int i= size - 1; i >= 0; i--) {
			char c= text.charAt(i);
			if (c == toRemove) {
				end= i;
			} else {
				break;
			}
		}
		if (end == size)
			return text;
		else if (end == 0)
			return ""; //$NON-NLS-1$
		else
			return text.substring(0, end);
	}
}
