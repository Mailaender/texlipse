/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.texlipse.TexlipsePlugin;
import org.eclipse.texlipse.spelling.engine.ISpellCheckEngine;
import org.eclipse.texlipse.spelling.engine.ISpellChecker;
import org.eclipse.texlipse.ui.PreferenceConstants;
import org.eclipse.texlipse.ui.TexUIMessages;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.texteditor.spelling.SpellingProblem;

/**
 * Proposal to add the unknown word to the dictionaries.
 *
 * @since 3.0
 */
public class AddWordProposal implements ITexCompletionProposal {

	private static final String PREF_KEY_DO_NOT_ASK= "do_not_ask_to_install_user_dictionary"; //$NON-NLS-1$

	/** The invocation context */
	private final IQuickAssistInvocationContext fContext;

	/** The word to add */
	private final String fWord;


	/**
	 * Creates a new add word proposal
	 *
	 * @param word
	 *                   The word to add
	 * @param context
	 *                   The invocation context
	 */
	public AddWordProposal(final String word, final IQuickAssistInvocationContext context) {
		fContext= context;
		fWord= word;
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public final void apply(final IDocument document) {

		final ISpellCheckEngine engine= SpellCheckEngine.getInstance();
		final ISpellChecker checker= engine.getSpellChecker();

		if (checker == null)
			return;

		if (!checker.acceptsWords()) {
			final Shell shell = fContext.getSourceViewer().getTextWidget().getShell(); // TODO NULL CHECKS REMOVED

			if (!canAskToConfigure() || !askUserToConfigureUserDictionary(shell))
				return;

			String[] preferencePageIds= new String[] { "org.eclipse.ui.editors.preferencePages.Spelling" }; //$NON-NLS-1$
			PreferencesUtil.createPreferenceDialogOn(shell, preferencePageIds[0], preferencePageIds, null).open();
		}

		if (checker.acceptsWords()) {
			checker.addWord(fWord);
			if (fContext != null && fContext.getSourceViewer() != null)
				SpellingProblem.removeAll(fContext.getSourceViewer(), fWord);
		}
	}

	/**
	 * Asks the user whether he wants to configure a user dictionary.
	 *
	 * @param shell the shell
	 * @return <code>true</code> if the user wants to configure the user dictionary
	 * @since 3.3
	 */
	private boolean askUserToConfigureUserDictionary(Shell shell) {
		MessageDialogWithToggle toggleDialog= MessageDialogWithToggle.openYesNoQuestion(
				shell,
				TexUIMessages.Spelling_add_askToConfigure_title,
				TexUIMessages.Spelling_add_askToConfigure_question,
				TexUIMessages.Spelling_add_askToConfigure_ignoreMessage,
				false,
				null,
				null);

		PreferenceConstants.getPreferenceStore().setValue(PREF_KEY_DO_NOT_ASK, toggleDialog.getToggleState());

		return toggleDialog.getReturnCode() == IDialogConstants.YES_ID;
	}

	/**
	 * Tells whether this proposal can ask to
	 * configure a user dictionary.
	 *
	 * @return <code>true</code> if it can ask the user
	 */
	static boolean canAskToConfigure() {
		return !PreferenceConstants.getPreferenceStore().getBoolean(PREF_KEY_DO_NOT_ASK);
	}


	@Override
	public String getAdditionalProposalInfo() {
		return MessageFormat.format(TexUIMessages.Spelling_add_info,
				new String[] { WordCorrectionProposal.getHtmlRepresentation(fWord) });
	}

	@Override
	public final IContextInformation getContextInformation() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return MessageFormat.format(TexUIMessages.Spelling_add_label, new String[] { fWord });
	}

	@Override
	public Image getImage() {
		return TexlipsePlugin.getImage("correction_add"); // TODO
	}

	/*
	 * @see org.eclipse.jdt.ui.text.java.IJavaCompletionProposal#getRelevance()
	 */
	@Override
	public int getRelevance() {
		return IProposalRelevance.ADD_WORD;
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getSelection(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public final Point getSelection(final IDocument document) {
		return new Point(fContext.getOffset(), fContext.getLength());
	}
}