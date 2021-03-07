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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.TextInvocationContext;
import org.eclipse.texlipse.editor.TeXSpellingReconcileStrategy;
import org.eclipse.texlipse.spelling.engine.ISpellCheckEngine;
import org.eclipse.texlipse.spelling.engine.ISpellChecker;
import org.eclipse.texlipse.spelling.engine.RankedWordProposal;
import org.eclipse.texlipse.ui.IQuickFixProcessor;
import org.eclipse.texlipse.ui.PreferenceConstants;

/**
 * Quick fix processor for incorrectly spelled words.
 *
 * @since 3.0
 */
public class WordQuickFixProcessor implements IQuickFixProcessor {

	/**
	 * Creates a new word quick fix processor.
	 */
	public WordQuickFixProcessor() {
		// For extension point
	}

	/*
	 * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#getCorrections(org.eclipse.jdt.ui.text.java.IInvocationContext,org.eclipse.jdt.ui.text.java.IProblemLocation[])
	 */
	@Override
	public ITexCompletionProposal[] getCorrections(IInvocationContext invocationContext, IProblemLocation[] locations) throws CoreException {

		final int threshold= PreferenceConstants.getPreferenceStore().getInt(PreferenceConstants.SPELLING_PROPOSAL_THRESHOLD);

		int size= 0;
		List<RankedWordProposal> proposals= null;
		String[] arguments= null;

		IProblemLocation location= null;
		RankedWordProposal proposal= null;
		ITexCompletionProposal[] result= null;

		boolean fixed= false;
		boolean match= false;
		boolean sentence= false;

		final ISpellCheckEngine engine= SpellCheckEngine.getInstance();
		final ISpellChecker checker= engine.getSpellChecker();

		if (checker != null) {

			for (int index= 0; index < locations.length; index++) {
				location= locations[index];

				ISourceViewer sourceViewer= null;
				if (invocationContext instanceof IQuickAssistInvocationContext)
					sourceViewer= ((IQuickAssistInvocationContext)invocationContext).getSourceViewer();
				IQuickAssistInvocationContext context= new TextInvocationContext(sourceViewer, location.getOffset(), location.getLength());

				if (location.getProblemId() == TeXSpellingReconcileStrategy.SPELLING_PROBLEM_ID) {

					arguments= location.getProblemArguments();
					if (arguments != null && arguments.length > 4) {

						sentence= Boolean.parseBoolean(arguments[3]);
						match= Boolean.parseBoolean(arguments[4]);
						fixed= arguments[0].charAt(0) == ITexTagConstants.TEX_TAG_PREFIX;

						if ((sentence && match) && !fixed)
							result= new ITexCompletionProposal[] { new ChangeCaseProposal(arguments, location.getOffset(), location.getLength(), context, engine.getLocale())};
						else {

							proposals= new ArrayList<>(checker.getProposals(arguments[0], sentence));
							size= proposals.size();

							if (threshold > 0 && size > threshold) {

								Collections.sort(proposals);
								proposals= proposals.subList(size - threshold - 1, size - 1);
								size= proposals.size();
							}

							boolean extendable= !fixed ? (checker.acceptsWords() || AddWordProposal.canAskToConfigure()) : false;
							result= new ITexCompletionProposal[size + (extendable ? 3 : 2)];

							for (index= 0; index < size; index++) {

								proposal= proposals.get(index);
								result[index]= new WordCorrectionProposal(proposal.getText(), arguments, location.getOffset(), location.getLength(), context, proposal.getRank());
							}

							if (extendable)
								result[index++]= new AddWordProposal(arguments[0], context);

							result[index++]= new WordIgnoreProposal(arguments[0], context);
							result[index++]= new DisableSpellCheckingProposal(context);
						}
						break;
					}
				}
			}
		}
		return result;
	}

	/*
	 * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#hasCorrections(org.eclipse.jdt.core.ICompilationUnit,int)
	 */
	@Override
	public final boolean hasCorrections(ICompilationUnit unit, int id) {
		return id == TeXSpellingReconcileStrategy.SPELLING_PROBLEM_ID;
	}

}
