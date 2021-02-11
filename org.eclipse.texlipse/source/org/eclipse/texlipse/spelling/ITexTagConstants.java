/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public interface ITexTagConstants {

	String TEX_CLOSE_PREFIX = "}";

	char[] TEX_ENTITY_CHARACTERS = new char[] { '\\', '{', '}' }; // TODO

	char TEX_ENTITY_START = '\\';

	char TEX_ENTITY_END = '}';

	String[] TEX_ENTITY_CODES = new String[] { "" };

	String[] TEX_GENERAL_TAGS = new String[] { "textbf", "section", "subsection", "paragraph" };

	char TEX_TAG_POSTFIX = '{';

	char TEX_TAG_PREFIX = '}';
}
