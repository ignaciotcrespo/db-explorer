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
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * This class provides a facade for the "save" FileDialog class. If the selected
 * file already exists, the user is asked to confirm before overwriting.
 * @author Sheel
 */
public class SafeSaveDialog {
  // The wrapped FileDialog
  protected FileDialog dlg; 
  protected Shell parent; 
  /**
   * SafeSaveDialog constructor
   * 
   * @param shell the parent shell
   */
  public SafeSaveDialog(Shell shell) {
    dlg = new FileDialog(shell, SWT.SAVE);
    parent = shell;
  }

  public String open() {
    // We store the selected file name in fileName
    String fileName = null;

    // The user has finished when one of the
    // following happens:
    // 1) The user dismisses the dialog by pressing Cancel
    // 2) The selected file name does not exist
    // 3) The user agrees to overwrite existing file
    boolean done = false;

    while (!done) {
      // Open the File Dialog
      fileName = dlg.open();
      if (fileName == null) {
        // User has cancelled, so quit and return
        done = true;
      } else {
        // User has selected a file; see if it already exists
    	if(!correctExt(fileName)) {
    		done=false;
    		continue;
    	}
        File file = new File(fileName);
        if (file.exists()) {
          // The file already exists; asks for confirmation
          MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING
              | SWT.YES | SWT.NO);

          // We really should read this string from a
          // resource bundle
          mb.setMessage(fileName + " already exists. Do you want to replace it?");

          // If they click Yes, we're done and we drop out. If
          // they click No, we redisplay the File Dialog
          done = mb.open() == SWT.YES;
        } else {
          // File does not exist, so drop out
          done = true;
        }
      }
    }
    return fileName;
  }

  protected boolean correctExt(String fileName) {
	return true;
}

public String getFileName() {
    return dlg.getFileName();
  }

  public String[] getFileNames() {
    return dlg.getFileNames();
  }

  public String[] getFilterExtensions() {
    return dlg.getFilterExtensions();
  }

  public String[] getFilterNames() {
    return dlg.getFilterNames();
  }

  public String getFilterPath() {
    return dlg.getFilterPath();
  }

  public void setFileName(String string) {
    dlg.setFileName(string);
  }

  public void setFilterExtensions(String[] extensions) {
    dlg.setFilterExtensions(extensions);
  }

  public void setFilterNames(String[] names) {
    dlg.setFilterNames(names);
  }

  public void setFilterPath(String string) {
    dlg.setFilterPath(string);
  }

  public Shell getParent() {
    return dlg.getParent();
  }

  public int getStyle() {
    return dlg.getStyle();
  }

  public String getText() {
    return dlg.getText();
  }

  public void setText(String string) {
    dlg.setText(string);
  }
}