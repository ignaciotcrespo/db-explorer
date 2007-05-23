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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * 
 * @author Sheel
 *
 */
public class AboutDialog {
	protected final Shell dialog ;
	protected final Shell parent;
	public AboutDialog(Shell parent) {
		this(parent,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
	}

	public AboutDialog(Shell parent, int style) {
		this.parent=parent;
		dialog = new Shell(parent,style);
	}

	
	public void open () {
 		
 		
			dialog.setLayout(new GridLayout(1,true));
			Label label = new Label(dialog, SWT.NONE);
			label.setImage(new Image(dialog.getDisplay(),dialog.getDisplay().getClass().getResourceAsStream(getAppLogo())));
			label.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false));
			addControls();
			label = new Label(dialog, SWT.NONE);
			label.setImage(new Image(dialog.getDisplay(),dialog.getDisplay().getClass().getResourceAsStream("/icon/logo-bottom.jpg")));
			label.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,true,false));
			dialog.setSize(501,392);
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
	 		
	}

	protected String getAppLogo() {
		return "";
	}

	protected void addControls() {
			
	}
	
}