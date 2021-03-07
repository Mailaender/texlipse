/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *     Mateusz Matela <mateusz.matela@gmail.com> - [code manipulation] [dcr] toString() builder wizard - https://bugs.eclipse.org/bugs/show_bug.cgi?id=26070
 *     Mateusz Matela <mateusz.matela@gmail.com> - [toString] Template edit dialog has usability issues - https://bugs.eclipse.org/bugs/show_bug.cgi?id=267916
 *     Mateusz Matela <mateusz.matela@gmail.com> - [toString] finish toString() builder wizard - https://bugs.eclipse.org/bugs/show_bug.cgi?id=267710
 *     Mateusz Matela <mateusz.matela@gmail.com> - [toString] toString() generator: Fields in declaration order - https://bugs.eclipse.org/bugs/show_bug.cgi?id=279924
 *******************************************************************************/
package org.eclipse.texlipse.ui;

import org.eclipse.osgi.util.NLS;

public final class TexUIMessages extends NLS {

	private static final String BUNDLE_NAME= "org.eclipse.texlipse.ui.TexUIMessages";//$NON-NLS-1$
	

	private TexUIMessages() {
		// Do not instantiate
	}

	// TODO: need to add translations
	public static String Spelling_error_label;
	public static String Spelling_correct_label;
	public static String Spelling_add_info;
	public static String Spelling_add_label;
	public static String Spelling_add_askToConfigure_title;
	public static String Spelling_add_askToConfigure_question;
	public static String Spelling_add_askToConfigure_ignoreMessage;
	public static String Spelling_ignore_info;
	public static String Spelling_ignore_label;
	public static String Spelling_disable_label;
	public static String Spelling_disable_info;
	public static String Spelling_case_label;
	public static String Spelling_error_case_label;
	public static String AbstractSpellingDictionary_encodingError;
	public static String Spelling_msgWithLocation;


	static {
		NLS.initializeMessages(BUNDLE_NAME, TexUIMessages.class);
	}

}
