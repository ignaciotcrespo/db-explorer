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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
/**
 * 
 * @author Sheel
 *
 */
public class SchemaDialog extends DBExplorerBaseDialog {

	List tables;
	Table coulmnsTable;
	Table keysTable;
	SashForm form=null;
	
	public SchemaDialog(Shell parent) {
		super(parent,SWT.SHELL_TRIM );
		
	}
	protected  void addButtons() {
		
		
	}
	public void addWidgets() {
		dialog.setLayout (new FillLayout());
		form = new SashForm(getDialog(), SWT.HORIZONTAL );
		
		form.setLayout(new FillLayout());
		
		Group grp = new Group(form,SWT.NONE);
		grp.setLayout(new FillLayout());
		grp.setText("Tables");
		tables = new List(grp, SWT.V_SCROLL | SWT.H_SCROLL);
		tables.setFont(new Font(parent.getDisplay(),"Verdana",9, SWT.NORMAL));
		
		CTabFolder folder = new CTabFolder(form, SWT.BORDER );
		folder.setLayoutData(new GridData(GridData.FILL, SWT.FILL, true, true));
		folder.setSimple(false);
		folder.setUnselectedImageVisible(false);
		folder.setUnselectedCloseVisible(false);
		
		CTabItem columns = new CTabItem(folder, SWT.BORDER | SWT.FILL);
		columns.setText("Columns");
		columns.setFont(new Font(parent.getDisplay(),"Verdana",10, SWT.BOLD));
		coulmnsTable = new Table(folder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.MULTI | SWT.FULL_SELECTION);
		coulmnsTable.setLinesVisible(true);
		coulmnsTable.setHeaderVisible(true);
		
		columns.setControl(coulmnsTable);
		
		CTabItem keys = new CTabItem(folder, SWT.BORDER | SWT.FILL);
		keys.setText("Keys");
		keys.setFont(new Font(parent.getDisplay(),"Verdana",10, SWT.BOLD));
		keysTable = new Table(folder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.MULTI | SWT.FULL_SELECTION);
		keysTable.setLinesVisible(true);
		keysTable.setHeaderVisible(true);
		
		keys.setControl(keysTable);
		
		folder.setSelection(columns);
		form.setWeights(new int[] {30,70});
		

	}
	
	public List getTableList() {
		return tables;
	}

	public Table getCoulmnsTable() {
		return coulmnsTable;
	}
	public Table getKeysTable() {
		return keysTable;
	}

	

}
