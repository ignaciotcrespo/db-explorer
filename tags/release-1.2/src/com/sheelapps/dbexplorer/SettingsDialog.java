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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author Sheel
 *
 */
public class SettingsDialog extends com.sheelapps.ui.common.BaseDialog {

	public SettingsDialog(Shell parent) {
		super(parent);
		
	}

	Text rows = null;
	int rowSize = 0;
	Combo driver =null;
	Text url = null;
	Text user = null;
	Text password = null;
	Text token = null;
	Text schema = null;
	
	String driverStr,urlStr, userStr, passwordStr, tokenStr, schemaStr;
	

	public Combo getDriver() {
		return driver;
	}

	public Text getPassword() {
		return password;
	}

	public Text getUrl() {
		return url;
	}

	public Text getUser() {
		return user;
	}
 
	
	

	
	
	 public Text getRowsText() {
		return rows;
	}

	public int getRowSize() {
		return rowSize;
	}

	public String getDriverStr() {
		return driverStr;
	}

	public String getPasswordStr() {
		return passwordStr;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public String getUserStr() {
		return userStr;
	}

	public Text getToken() {
		return token;
	}

	public String getTokenStr() {
		return tokenStr;
	}

	protected void handleOkAction() {
		
		
				try {
					rowSize = Integer.parseInt(rows.getText());
				}catch(Exception e){
					rowSize=50;
				}
				driverStr = driver.getText();
				urlStr = url.getText();
				userStr = user.getText();
				passwordStr = password.getText();
				tokenStr =  token.getText();
				if(tokenStr==null || tokenStr.trim().equals("") || tokenStr.trim().length() > 1) tokenStr=";" ;
				schemaStr = schema.getText();
				dialog.close();
	}

	protected void setDialogSize() {
		Rectangle displayRect = parent.getBounds();
		dialog.setSize((int)(displayRect.width * 0.5), (int)(displayRect.height * 0.5));
		dialog.setMinimumSize(500, 500);
	}
	protected void addComponents() {
		//dialog.setImage(new Image(parent.getDisplay(),parent.getDisplay().getClass().getResourceAsStream("/icon/library.gif")));
		dialog.setText("DB Explorer Settings");
		rows = new Text(addGroup("Rows (Set -1 for all rows)",1),SWT.BORDER );
		rows.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		rows.addListener (SWT.Verify, new Listener () {
			public void handleEvent (Event e) {
				String string = e.text;
				char [] chars = new char [string.length ()];
				string.getChars (0, chars.length, chars, 0);
				for (int i=0; i<chars.length; i++) {
					if (!(('0' <= chars [i] && chars [i] <= '9') || chars[0]=='-')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
	driver = new Combo(addGroup("JDBC Driver",1),SWT.DROP_DOWN);
	driver.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
    driver.addSelectionListener(
			  new SelectionAdapter()
			  {
			    public void widgetSelected(SelectionEvent e)
			    {
			      driverStr = driver.getText();
			    }
			  }
			 );
    url = new Text(addGroup("Database URL",1),SWT.BORDER );
	url.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	user = new Text(addGroup("Database User",1),SWT.BORDER );
	user.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	password = new Text(addGroup("Database Password",1),SWT.BORDER | SWT.PASSWORD);
	password.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
	schema = new Text(addGroup("Default Schema",1),SWT.BORDER );
	schema.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
	token = new Text(addGroup("SQL Separator (Token to separate multiple SQLs in a tab)",1),SWT.BORDER | SWT.BORDER);
	token.setTextLimit(1);
	token.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
	
		
	}
	
	protected void handleCancelAction() {
		dialog.close();
		
	}

	public String getSchemaStr() {
		return schemaStr;
	}

	public Text getSchema() {
		return schema;
	}

	
}
