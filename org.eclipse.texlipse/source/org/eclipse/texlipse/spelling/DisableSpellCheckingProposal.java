/*******************************************************************************
 * Copyright (c) 2007, 2012 IBM Corporation and others.
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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.texlipse.TexlipsePlugin;
import org.eclipse.texlipse.ui.TexUIMessages;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.spelling.SpellingService;


public class DisableSpellCheckingProposal implements ITexCompletionProposal {

	private IQuickAssistInvocationContext fContext;


	public DisableSpellCheckingProposal(IQuickAssistInvocationContext context) {
		fContext= context;
	}

	@Override
	public final void apply(final IDocument document) {
		IPreferenceStore store= EditorsUI.getPreferenceStore();
		store.setValue(SpellingService.PREFERENCE_SPELLING_ENABLED, false);
	}

	@Override
	public String getAdditionalProposalInfo() {
		return TexUIMessages.Spelling_disable_info;
	}

	@Override
	public final IContextInformation getContextInformation() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return TexUIMessages.Spelling_disable_label;
	}

	@Override
	public Image getImage() {
		return TexlipsePlugin.getImage("replacetext");
	}

	@Override
	public final int getRelevance() {
		return IProposalRelevance.DISABLE_SPELL_CHECKING;
	}

	@Override
	public final Point getSelection(final IDocument document) {
		return new Point(fContext.getOffset(), fContext.getLength());
	}
}
