/*
 * Copyright 2006,2007 Sheel Khanna.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is Sheel Khanna. Portions created by
 * the Initial Developer are Copyright (C) 2006, 2007 by Sheel Khanna.
 * All Rights Reserved.
 * 
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *  
 */

package com.sheelapps.dbexplorer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Sheel
 *
 */
public class HistoryDialog extends DBExplorerBaseDialog {
   Text sqls = null;
	public HistoryDialog(Shell parent) {
		super(parent,SWT.DIALOG_TRIM |  SWT.APPLICATION_MODAL | SWT.RESIZE);
		
	}

	public HistoryDialog(Shell parent, int style) {
		super(parent, style);
		
	}
	protected  void addButtons() {
		
	}
	public void addWidgets() {
		dialog.setLayout(new FillLayout());
		 sqls = new Text(dialog, SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP);
		 sqls.setFont(new Font(getDialog().getDisplay(),"Verdana",10, SWT.NORMAL));
		
		
	}

	public Text getSqls() {
		return sqls;
	}
	
}