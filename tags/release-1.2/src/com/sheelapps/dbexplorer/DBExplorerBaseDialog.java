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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Sheel
 *
 */
public abstract class DBExplorerBaseDialog  {
	boolean okClicked=false;
	final Shell dialog ;
	final Shell parent;
	
	public DBExplorerBaseDialog(Shell parent, int style) {
		this.parent = parent;
		dialog = new Shell(parent, style);
	}
	
	public DBExplorerBaseDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM |  SWT.APPLICATION_MODAL );
		dialog.setLayout (new GridLayout (2,false));
	}
	
	public void doLayout() {
		dialog.setImage(new Image(parent.getDisplay(),parent.getDisplay().getClass().getResourceAsStream("/icon/icon.jpg")));
 		addWidgets();
 		addButtons();
		
	}
	
	protected  void addButtons() {
		final Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Ok");
		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		Listener listener =new Listener () {
			public void handleEvent (Event event) {
				
				if( event.widget == ok) {
					okClicked = true;
				}else {
					okClicked = false;
				}
				processBeforeClosing();
				dialog.close ();
			}

		};
		ok.addListener (SWT.Selection, listener);
		cancel.addListener (SWT.Selection, listener);
		
	}

	protected void processBeforeClosing() {
		
		
	}

	public boolean open () {
		 		
		 		
			//	dialog.pack();
				dialog.setSize((int)(parent.getSize().x * 0.75),(int)(parent.getSize().y * 0.75));
				Rectangle splashRect = dialog.getBounds();
				Rectangle displayRect = parent.getDisplay().getBounds();
				int x = (displayRect.width - splashRect.width) / 2;
				int y = (displayRect.height - splashRect.height) / 2;
				dialog.setLocation(x, y);
				dialog.open();
		 		Display display = parent.getDisplay();
		 		while (!dialog.isDisposed()) {
		 			if (!display.readAndDispatch()) display.sleep();
		 		}
		 		return okClicked;
		 	}

	
	public boolean isOkClicked() {
		return okClicked;
	}

	public abstract void addWidgets() ;

	public void setTitle(String str) {
		dialog.setText(str);
		
	} 
	
	protected Shell getDialog() {
		return dialog;
	}

}
