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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author Sheel
 *
 */
public class SelectableTableListener implements Listener {


	Table table;
	TableEditor editor;
	public SelectableTableListener(Table table) {
		this.table = table;
		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}

	 public void handleEvent(Event event) {
	        Rectangle clientArea = table.getClientArea();
	        Point pt = new Point(event.x, event.y);
	        int index = table.getTopIndex();
	        while (index < table.getItemCount()) {
	          boolean visible = false;
	          final TableItem item = table.getItem(index);
	          //table.setSelection(
	          for (int i = 0; i < table.getColumnCount(); i++) {
	            Rectangle rect = item.getBounds(i);
	            if (rect.contains(pt)) {
	             // final int column = i;
	              final Text text = new Text(table, SWT.NONE);
	              Listener textListener = new Listener() {
	                public void handleEvent(final Event e) {
	                  switch (e.type) {
	                  case SWT.FocusOut:
	                  //  item.setText(column, text.getText());
	                    text.dispose();
	                    break;
	                  case SWT.Traverse:
	                    switch (e.detail) {
	                    case SWT.TRAVERSE_RETURN:
	                    //  item.setText(column, text.getText());
	                    // FALL THROUGH
	                    case SWT.TRAVERSE_ESCAPE:
	                      text.dispose();
	                      e.doit = false;
	                    }
	                    break;
	                  }
	                }
	              };
	              text.addListener(SWT.FocusOut, textListener);
	              text.addListener(SWT.Traverse, textListener);
	              editor.setEditor(text, item, i);
	              text.setText(item.getText(i));
	              text.selectAll();
	              text.setFocus();
	              return;
	            }
	            if (!visible && rect.intersects(clientArea)) {
	              visible = true;
	            }
	          }
	          if (!visible)
	            return;
	          index++;
	        }
	      }



}
