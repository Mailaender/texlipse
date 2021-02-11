/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

import java.net.URL;

import org.eclipse.texlipse.spelling.engine.AbstractSpellDictionary;


public class TexTagDictionary extends AbstractSpellDictionary {

	@Override
	protected final URL getURL() {
		return null;
	}

	public boolean isCorrect(final String word) {

		if (word.charAt(0) == ITexTagConstants.TEX_TAG_PREFIX)
			return super.isCorrect(word);

		return false;
	}

	@Override
	protected synchronized boolean load(final URL url) {

		unload();

		for (String tag : ITexTagConstants.TEX_GENERAL_TAGS) {
			hashWord(ITexTagConstants.TEX_TAG_PREFIX + tag + ITexTagConstants.TEX_TAG_POSTFIX);
			hashWord(ITexTagConstants.TEX_CLOSE_PREFIX + tag + ITexTagConstants.TEX_TAG_POSTFIX);
		}
		return true;
	}

	@Override
	protected String stripNonLetters(String word) {
		return word;
	}

}
