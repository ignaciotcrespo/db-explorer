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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Sheel
 *
 */
public class AboutDialog extends com.sheelapps.ui.common.AboutDialog {

	public AboutDialog(Shell parent) {
		this(parent,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
	}
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		
	}
	
	protected void addControls() {
		
		CTabFolder folder = new CTabFolder(dialog, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		folder.setSimple(false);
		folder.setUnselectedImageVisible(false);
		folder.setUnselectedCloseVisible(false);
		
		CTabItem item = new CTabItem(folder,  SWT.V_SCROLL | SWT.H_SCROLL);
		item.setText("Help/Features");
		item.setFont(new Font(dialog.getDisplay(),"Verdana",10, SWT.BOLD));
		Label text = new Label(folder, SWT.BORDER | SWT.WRAP );
		text.setText( "DB Explorer is an easy-to-use tool that lets you manage any JDBC compliant database. \n "+
				  "- Support any Database with JDBC driver. Copy driver's jar file to <install-dir>/ext-jars folder and provide database settings in File->Settings Dialog.\n "+
			      "- Support multiple queries per tab. Use Separator token to separate multiple SQLs.\n"+
			      "- Limit number of rows in query result. Default is 50 rows.\n"+
			      "- Support comments in SQL. Use /* */ to comment any part of SQL.\n"+
			      "- Powerful Database Schema Explorer.\n"+
			      "- Enter <table-name> to execute simple SELECT query.\n"+
			      "- View previously Executed SQLs in History Dialog.\n"+
			      "- Tray hide/unhide support.\n"+
			      "- Export query result to CSV file.\n"+
			      "- Import CSV file."
			      );
		item.setControl(text);
		folder.setSelection(item);
		item = new CTabItem(folder,  SWT.V_SCROLL | SWT.H_SCROLL);
		item.setText("License");
		item.setFont(new Font(dialog.getDisplay(),"Verdana",10, SWT.BOLD));
		text = new Label(folder, SWT.BORDER );
		text.setText("This product is released under our Freeware license. \n " +
				"See license.txt for more information.");
		
		item.setControl(text);
		
	
	}
	
	public String getAppLogo() {
		return "/icon/logo-top.jpg";
	}
}