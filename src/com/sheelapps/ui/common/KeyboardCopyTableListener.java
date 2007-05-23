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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
/**
 * Enables copy of multiple rows using Ctrl+C. 
 * @author Sheel
 *
 */
public class KeyboardCopyTableListener extends KeyAdapter {
	Table table;
	
	public KeyboardCopyTableListener(Table table) {
		this.table = table;
	
	}
	public void keyPressed(KeyEvent e) {
		    	  // CTRL+C
		        if(e.stateMask == SWT.CTRL && e.keyCode==99) {
		        	StringBuffer data = new StringBuffer();
		        			TableItem item[] = table.getSelection();
		        			if(item!=null && item.length > 0) {
		        				for(int i=0; i < item.length ;i++) {
		        				 for(int col=0; col < table.getColumnCount();col++) {	
		        					 data.append(item[i].getText(col)+" ");
		        				  }
		        				 data.append(System.getProperty("line.separator"));
		        				}
		        			}
		     //  System.out.println(data);
		       setClipboardContents(data.toString());
		        }
      }


	 public void setClipboardContents( String aString ){
		    StringSelection stringSelection = new StringSelection( aString );
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents( stringSelection, null );
  } 
	 
}
