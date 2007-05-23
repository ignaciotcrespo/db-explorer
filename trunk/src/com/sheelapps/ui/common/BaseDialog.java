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

package com.sheelapps.ui.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
/**
 * 
 * @author Sheel
 *
 */
public abstract class BaseDialog  {
	
	protected Shell dialog,parent;
	protected boolean okClicked;
	private Composite composite = null;
	
	public BaseDialog(Shell parent,int style) {
		this.parent=parent;
		dialog = new Shell(parent,style);
	}
	public BaseDialog(Shell parent) { 
		this(parent,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
/**
 * Call this method to build
 *
 */
	public void buildUI() {
		setLayout();
		addUI();
		addComponents();
		if(!dialog.isDisposed()) {
		addOkCancelButtons();
		setDialogSize();
		setDialogLocation();
		}
	}

	protected void addUI() {
		getMainComposite();
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		composite.setLayoutData(gd);
		GridLayout lg = new GridLayout(1,true);
		composite.setLayout(lg);
		
	}
	
	protected Composite getMainComposite() {
		if(composite==null)
		 composite = new Composite(dialog,SWT.BORDER);
		return composite;
	}
	protected Group addGroup(String title,int children) {
		return addGroup(title,children,SWT.NONE,composite);
	}
	
	protected Group addGroup(String title,int children,Composite compParent) {
		return addGroup(title,children,SWT.NONE,compParent);
	}
	
	protected Group addGroup(String title, int children, int style,Composite compParent) {
		Group grp = new Group(compParent, style);
		grp.setText(title);
		grp.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		grp.setLayout(new GridLayout(children,false));
		return grp;
	}
	
	public void open() {
		
		if(!dialog.isDisposed()) {
		dialog.open();
 		Display display = parent.getDisplay();
 		while (!dialog.isDisposed()) {
 			if (!display.readAndDispatch()) display.sleep();
 		}
	   }
	}
	protected void setDialogLocation() {
		Rectangle splashRect = dialog.getBounds();
		Rectangle displayRect = parent.getDisplay().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		dialog.setLocation(x, y);
		
	}
	protected void setDialogSize() {
		Rectangle displayRect = parent.getDisplay().getBounds();
		dialog.setSize((int)(displayRect.width * 0.75), (int)(displayRect.height * 0.75));
		
	}
	protected void addOkCancelButtons() {
		Composite btnComp = new Composite(dialog,SWT.None);
		GridData gd = new GridData(SWT.END,SWT.END,true,false);
		btnComp.setLayoutData(gd);
		//btnComp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		FillLayout l = new FillLayout();
		l.spacing=10;
		btnComp.setLayout(l);
		
		final Button ok = new Button (btnComp, SWT.PUSH);
		ok.setText (getOKButtonText());
		
		
		Button cancel = new Button (btnComp, SWT.PUSH);
		cancel.setText ("Cancel");
		
		Listener listener =new Listener () {
			public void handleEvent (Event event) {
				
				if( event.widget == ok) {
					okClicked = true;
					handleOkAction();
				}else {
					okClicked = false;
					handleCancelAction();
				}
				
				
			}

		};
		ok.addListener (SWT.Selection, listener);
		cancel.addListener (SWT.Selection, listener);
		dialog.setDefaultButton(ok);
	}
	protected String getOKButtonText() {
		return "Ok";
	}
	protected void handleCancelAction() {
		dialog.close();
		
	}
	protected abstract void addComponents() ;
	
	protected abstract void handleOkAction() ;
	
	
	protected void setLayout() {
		dialog.setLayout (new GridLayout (1,true));
	}
	
	
	protected void showMessage(String title, String message) {
		MessageBox box = new MessageBox(dialog,SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}
	
	protected  void showException(String title, Exception e) {
		StringWriter stacktrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stacktrace));
		showError(title,stacktrace.toString().replace('\r', ' ').replace('\t', ' '));
	}
	
	protected void showError(String title, String message) {
		
		MessageBox box = new MessageBox(dialog,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
		box.setText(title);
		box.setMessage(message);
		box.open();

	}
	
	public boolean isOkClicked() {
		
		return okClicked;
	}

}
