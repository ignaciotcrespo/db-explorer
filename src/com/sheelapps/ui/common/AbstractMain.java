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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
/**
 * 
 * @author Sheel
 *
 */
public abstract class AbstractMain {
	protected static Display display;
	protected static Shell shell;
	
	
	public AbstractMain() {
		
	}

	public final void startApplication() {
		display = new Display();
		shell = new Shell(display,getShellStyle());
		showSplash();
		shell.setLayout(getShellLayout());
		setTitleAndIcon();
		setMenu();
		setToolbar();
		setComponents();
		setTray();
		setSize();
		setLocation();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		
	}
 
	protected int getShellStyle() {
		return SWT.SHELL_TRIM;
		
	}

	private Layout getShellLayout() {
			return new FillLayout();
	}

	protected void setTray() {

		 final Tray tray = display.getSystemTray ();
			if (tray != null) {
				final TrayItem item = new TrayItem (tray, SWT.NONE);
				item.setToolTipText(getTrayText());
				item.addListener (SWT.Show, new Listener () {
					public void handleEvent (Event event) {
						//System.out.println("show");
					}
				});
				item.addListener (SWT.Hide, new Listener () {
					public void handleEvent (Event event) {
						//System.out.println("hide");
					}
				});
				item.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						shell.setVisible(!shell.isVisible());
						if(shell.isVisible()) {
						 	 shell.setActive();
						 
						}
						
					}
				});
				item.addListener (SWT.DefaultSelection, new Listener () {
					public void handleEvent (Event event) {
						//System.out.println("default selection");
					}
				});
				
					final Menu menu = new Menu (shell, SWT.POP_UP);
					addTrayMenuItem(menu);
					
				
				item.addListener (SWT.MenuDetect, new Listener () {
					public void handleEvent (Event event) {
						menu.setVisible (true);
						
					}
				});
				if(getApplicationIcon()!=null)
				  item.setImage (getApplicationIcon());
			}
	 
		
	}

	private void addTrayMenuItem(Menu menu) {
		MenuItem mi = new MenuItem (menu, SWT.PUSH);
		mi.setText ("Exit" );

		//mi.setImage(new Image(display, display.getClass().getResourceAsStream("/icon/exit.gif")));
		// mi.setAccelerator(SWT.ALT + 'X');
		mi.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
				display.dispose();
			}
		});
		
	}

	protected String getTrayText() {
		return "Sheel Apps";
	}

	protected void showSplash() {

	}
	

	protected void setLocation() {
		Rectangle splashRect = shell.getBounds();
		Rectangle displayRect = display.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		shell.setLocation(x, y);
		
	}

	protected void setSize() {
		shell.setSize((int)(display.getBounds().width * 0.75), (int) (display.getBounds().height * 0.75));
		
	}

	protected abstract void setComponents() ;

	protected abstract void setToolbar() ;

	protected abstract void setMenu();

	private void setTitleAndIcon() {
		shell.setText(getApplicationTitle());
		if(getApplicationIcon()!=null)
			shell.setImage(getApplicationIcon());
	}

	protected String getApplicationTitle() {
		
		return "";
	}

	public  Image getApplicationIcon() {
		return null;
	}

	protected static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		//x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int)Math.sqrt(r*r - x*x);
			polygon[2*i] = offsetX + x;
			polygon[2*i+1] = offsetY + y;
			polygon[8*r - 2*i - 2] = offsetX + x;
			polygon[8*r - 2*i - 1] = offsetY - y;
		}
		return polygon;
	}
	
}
