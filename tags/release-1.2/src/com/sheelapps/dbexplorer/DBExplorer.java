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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.sheelapps.ui.common.KeyboardCopyTableListener;
import com.sheelapps.ui.common.SelectableTableListener;
import com.sheelapps.ui.common.Splash;

/**
 * Main class of DB Explorer. 
 * @author Sheel
 *
 */
public class DBExplorer {
	private CTabFolder folder = null;

	private static int connCounter = 1;

	private int tabCount = 0;

	private static Display display = null;

	private static Shell shell = null;

	private String sqlFile = System.getProperty("user.home") + File.separator
			+ "dbexplorer.sql";

	Vector queries = new Vector();

	int currentTab = 0;

	int maxRows = 50;

	static Vector allTables = new Vector();

	static String driver = null;

	static String url = null;

	static String user = null;

	static String password = null;

	static String schema = null;

	private Vector commonDrivers = new Vector();

	private Vector sessConns = new Vector();

	private String token = ";";

	private Menu connSubMenu;

	private static boolean isInit = false;

	public DBExplorer() {
		doInit();
	}

	private void doInit() {
		queries
				.add("***** To load your pre-defined sqls from a file: Create a java property file : '"
						+ sqlFile
						+ "' with entries like <anystr>=<your-sql> *****");
		try {
			Properties p = new Properties();
			// System.out.println(sqlFile);
			p.load(new FileInputStream(sqlFile));
			if (p.size() > 0) {
				queries.addAll(p.entrySet());
			}
		} catch (Exception e) {

		}
		driver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/test";
		user = "";
		password = "";
		schema = "";

		commonDrivers.add("com.mysql.jdbc.Driver");
		commonDrivers.add("org.hsqldb.jdbcDriver");
		commonDrivers.add("sun.jdbc.odbc.JdbcOdbcDriver");
		commonDrivers.add("COM.ibm.db2.jdbc.app.DB2Driver");
		commonDrivers.add("COM.ibm.db2.jdbc.net.DB2Driver");
		commonDrivers.add("com.informix.jdbc.IfxDriver");
		commonDrivers.add("org.gjt.mm.mysql.Driver");
		commonDrivers.add("oracle.jdbc.driver.OracleDriver");
		commonDrivers.add("com.pointbase.jdbc.jdbcUniversalDriver");
		commonDrivers.add("org.postgresql.Driver");

	}

	public static void loadSchemaTables() {
		allTables = new Vector();
		Connection conn = null;
		try {
			conn = getConnection(true);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				ResultSet rs = null;
				if (schema != null && schema.length() > 0)
					rs = meta.getTables(null, schema, null, new String[] {
							"TABLE", "VIEW" });
				else
					rs = meta.getTables(null, null, null, new String[] {
							"TABLE", "VIEW" });
				while (rs.next()) {
					allTables.add(rs.getString(3));
				}
				Collections.sort(allTables);

			}
		} catch (SQLException se) {
			if (isInit)
				showError("SQL Error Code : " + se.getErrorCode()
						+ "\nMessage : " + se.getMessage());
		} catch (ClassNotFoundException ce) {
			if (isInit)
				showError("Failed to load the specified JDBC driver : "
						+ ce.getMessage()
						+ "\nCopy JDBC driver to <install-dir>/ext-jars folder.");
		} catch (Exception e) {
			// e.printStackTrace();
			// ExceptionDialog(e);
			if (isInit)
				ExceptionDialog(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// e.printStackTrace();
				}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DBExplorer app = new DBExplorer();
		app.start();
	}

	/**
	 * Initialize UI.
	 * 
	 */
	private void start() {

		display = new Display();
		Splash.showSplash(display, "/icon/logo.jpg");

		shell = new Shell(display);

		shell.setLayout(new GridLayout());
		shell.setText("DB Explorer - " + url + ", User=" + user);
		shell.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/icon.jpg")));
		buildMenu();
		addCoolToolbar();
		addTabFolder();
		addTab();
		setTray();

		shell.setMaximized(true);

		shell.open();
		isInit = true;
		showSettings();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	/**
	 * Add toolbar.
	 */
	private void addCoolToolbar() {

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		FormData coolData = new FormData();
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top = new FormAttachment(0);
		// coolBar.setLayoutData(coolData);
		coolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				shell.layout();
			}
		});
		ToolBar toolBar = new ToolBar(coolBar, SWT.HORIZONTAL);
		toolBar.setLayout(new RowLayout());
		// toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/open.gif")));
		item.setToolTipText("Add DB Tab");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				addTab();
			}
		});
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/folder.gif")));
		item.setToolTipText("Close DB Tab");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				closeTab();
			}
		});
		item = new ToolItem(toolBar, SWT.SEPARATOR);

		toolBar.pack();
		Point size = toolBar.getSize();
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(toolBar);
		Point preferred = coolItem.computeSize(size.x, size.y);
		coolItem.setPreferredSize(preferred);
		coolItem.setSize(preferred);

		toolBar = new ToolBar(coolBar, SWT.HORIZONTAL);
		toolBar.setLayout(new RowLayout());
		// toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// item = new ToolItem(toolBar,SWT.SEPARATOR);

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/properties.gif")));
		item.setToolTipText("Settings");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				showSettings();
			}
		});

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/save.jpg")));
		item.setToolTipText("Export To CSV");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				exportAsCSV();
			}
		});

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/import.jpg")));
		item.setToolTipText("Import From CSV");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				importFromCSV();
			}
		});

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(new Image(display, display.getClass()
				.getResourceAsStream("/icon/run.gif")));
		item.setToolTipText("Execute Query");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				executeQuery();
			}
		});
		item = new ToolItem(toolBar, SWT.SEPARATOR);

		toolBar.pack();
		size = toolBar.getSize();
		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(toolBar);
		preferred = coolItem.computeSize(size.x, size.y);
		coolItem.setPreferredSize(preferred);
		coolItem.setSize(preferred);

		/*
		 * toolBar = new ToolBar(coolBar, SWT.HORIZONTAL); toolBar.setLayout(new
		 * RowLayout());
		 * 
		 * item = new ToolItem(toolBar,SWT.SEPARATOR); final Combo comboTables =
		 * new Combo(toolBar, SWT.READ_ONLY); if(allTables.size()==0)
		 * loadSchemaTables(); for (int i = 0; i < allTables.size(); i++) {
		 * comboTables.add(allTables.get(i).toString()); } comboTables.pack();
		 * toolBar.pack(); item.setWidth(comboTables.getSize().x+1);
		 * item.setControl(comboTables); comboTables.addSelectionListener( new
		 * SelectionAdapter() { public void widgetSelected(SelectionEvent e) {
		 * if(getCurrentSQLText()!=null) {
		 * getCurrentSQLText().setText(comboTables.getText()); } } } ); item =
		 * new ToolItem(toolBar,SWT.SEPARATOR); toolBar.pack(); size =
		 * toolBar.getSize(); coolItem = new CoolItem(coolBar, SWT.NONE);
		 * coolItem.setControl(toolBar); preferred =
		 * coolItem.computeSize(size.x, size.y);
		 * coolItem.setPreferredSize(preferred); coolItem.setSize(preferred);
		 */
		coolBar.pack();
	}

	private void addTabFolder() {
		folder = new CTabFolder(shell, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		folder.setSimple(false);
		folder.setUnselectedImageVisible(false);
		folder.setUnselectedCloseVisible(false);
		Menu submenu = new Menu(shell, SWT.POP_UP);
		folder.setMenu(submenu);
		MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Add DB &Tab\tCtrl+T");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/folder.gif")));
		item.setAccelerator(SWT.CTRL + 'T');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				addTab();
			}
		});

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Close &DB Tab\tCtrl+D");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/delete.gif")));
		item.setAccelerator(SWT.CTRL + 'D');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				closeTab();
			}
		});

		folder.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				addTab();

			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void buildMenu() {
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&File");

		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);

		MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Add DB &Tab\tCtrl+T");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/folder.gif")));
		item.setAccelerator(SWT.CTRL + 'T');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				addTab();
			}
		});

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Close &DB Tab\tCtrl+D");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/delete.gif")));
		item.setAccelerator(SWT.CTRL + 'D');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				closeTab();
			}
		});

		new MenuItem(submenu, SWT.SEPARATOR);
		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("&Schema Explorer...\tCtrl+S");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/open.gif")));
		item.setAccelerator(SWT.CTRL + 'S');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				showSchema();
			}
		});

		new MenuItem(submenu, SWT.SEPARATOR);
		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("S&ettings...\tCtrl+E");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/parameter.gif")));
		item.setAccelerator(SWT.CTRL + 'E');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				showSettings();
			}
		});
		new MenuItem(submenu, SWT.SEPARATOR);

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Export To CSV...\tCtrl+R");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/class.gif")));
		item.setAccelerator(SWT.CTRL + 'R');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				exportAsCSV();
			}
		});

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("I&mport From CSV...\tCtrl+M");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/class.gif")));
		item.setAccelerator(SWT.CTRL + 'M');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				importFromCSV();
			}
		});

		new MenuItem(submenu, SWT.SEPARATOR);

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("E&xit\tALT+X");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/exit.gif")));
		item.setAccelerator(SWT.ALT + 'X');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
				display.dispose();
			}
		});

		// Second Menu
		item = new MenuItem(bar, SWT.CASCADE);
		item.setText("Quer&y");
		submenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(submenu);

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("Execute Query\tCtrl+Q");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/execute.gif")));
		item.setAccelerator(SWT.CTRL + 'Q');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				executeQuery();
			}
		});

		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("SQL History...\tCtrl+H");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/properties.gif")));
		item.setAccelerator(SWT.CTRL + 'H');
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				executedSQLs();
			}
		});

		item = new MenuItem(submenu, SWT.CASCADE);
		item.setText("Connections...");

		connSubMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(connSubMenu);
		addSessionConn();

		// Third Menu
		item = new MenuItem(bar, SWT.CASCADE);
		item.setText("&Help");
		submenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(submenu);
		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("&About...");
		// item.setImage(new Image(display,
		// display.getClass().getResourceAsStream("/icon/activate.gif")));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				about();
			}

		});
	}

	/**
	 * Adds Connection MenuItem.
	 * 
	 */
	private void addSessionConn() {
		MenuItem item = null;
		while (connSubMenu != null && connSubMenu.getItemCount() > 0) {
			int lastOne = connSubMenu.getItemCount() - 1;
			connSubMenu.getItem(lastOne).dispose();
		}

		SessionConnection scon = null;
		for (int i = 0; i < sessConns.size(); i++) {
			item = new MenuItem(connSubMenu, SWT.RADIO);
			scon = (SessionConnection) sessConns.get(i);
			item.setText(scon.id + "=" + scon.url + ", User=" + scon.user);
			item.setData(scon);
			if (scon.url.equals(url) && scon.user.equals(user)) {
				item.setSelection(true);
			}
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					SessionConnection selcon = (SessionConnection) event.widget
							.getData();
					driver = selcon.driver;
					url = selcon.url;
					user = selcon.user;
					password = selcon.password;
					allTables.clear();
					shell.setText("DB Explorer - " + url + ", User=" + user);

				}

			});
		}

	}

	protected void importFromCSV() {
		ImportCSVDialog iDlg = new ImportCSVDialog(shell);
		iDlg.buildUI();
		iDlg.open();

	}
/**
 * The first table in result window.
 * @param control
 * @return
 */
	private Table getFirstTable(Control control) {
		Table table = null;
		if (control != null) {
			if (control instanceof Composite) {
				if (((Composite) control).getChildren() != null
						&& ((Composite) control).getChildren().length > 0) {
					SashForm form = (SashForm) ((Composite) control)
							.getChildren()[0];
					if (form != null)
						table = (Table) form.getChildren()[0];
				}
			}
		}
		return table;
	}
/**
 * Copy executed SQLs to history dialog.
 *
 */
	protected void executedSQLs() {
		HistoryDialog dlg = new HistoryDialog(shell);
		dlg.setTitle("DB Explorer");
		dlg.doLayout();
		StringBuffer s = new StringBuffer("");
		for (int i = 0; i < queries.size(); i++) {
			s.append("(" + (i + 1) + ") " + queries.get(i).toString()
					+ System.getProperty("line.separator")
					+ System.getProperty("line.separator"));
		}
		dlg.getSqls().setText(s.toString());
		dlg.open();

	}

	/**
	 * Export first table from result to CSV file.
	 *
	 */
	protected void exportAsCSV() {
		Control comp = getCurrentResultAreaControl();
		Table table = getFirstTable(comp);

		if (table == null) {
			return;
		}
		int colCount = table.getColumnCount();
		if (colCount == 0)
			return;

		SafeSaveDialog save = new SafeSaveDialog(shell);
		save.setText("DB Explorer - Export To CSV");
		save.setFilterExtensions(new String[] { "*.csv", "*.txt", "*.*" });
		String filename = save.open();
		PrintWriter fout = null;
		if (filename != null) {
			try {
				fout = new PrintWriter(new FileOutputStream(filename));
				for (int i = 0; i < colCount; i++) {
					if (table.getColumn(i).getText().indexOf("(") > 0)
						fout.print("\""
								+ table.getColumn(i).getText().substring(
										0,
										table.getColumn(i).getText().indexOf(
												"(")) + "\"");
					else
						fout.print("\"" + table.getColumn(i).getText() + "\"");
					if (i < (colCount - 1))
						fout.print(",");
				}
				fout.println();
				int index = 0;
				while (index < (table.getItemCount() - 1)) {
					TableItem item = table.getItem(index);
					for (int i = 0; i < colCount; i++) {
						fout.print("\""
								+ item.getText(i).replaceAll("\"", "\"\"")
								+ "\"");
						if (i < (colCount - 1))
							fout.print(",");
					}
					fout.println();
					index++;
				}
				fout.close();
				showMessage("Data exported to " + filename + ".");
			} catch (FileNotFoundException e) {
				ExceptionDialog(e);
			} finally {
				if (fout != null)
					fout.close();
			}

		}

	}

	protected void about() {
		final AboutDialog about = new AboutDialog(shell);
		about.open();

	}

	/**
	 * Open Schema dialog
	 *
	 */
	protected void showSchema() {
		if (allTables.size() == 0)
			loadSchemaTables();
		if (allTables.size() == 0)
			return;
		final SchemaDialog schemaDlg = new SchemaDialog(shell);
		schemaDlg.setTitle("Schema Explorer");
		schemaDlg.doLayout();
		for (int i = 0; i < allTables.size(); i++) {
			schemaDlg.getTableList().add(allTables.get(i).toString());
		}
		// schema.getDialog().pack();
		schemaDlg.getTableList().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				List list = (List) e.getSource();
				String[] str = list.getSelection();
				for (int i = 0; i < str.length; i++) {
					shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));

					Connection conn = null;
					try {
						conn = getConnection(false);
						DatabaseMetaData meta = conn.getMetaData();
						ResultSet rs = null;
						// System.out.println("schema "+schema);
						if (schema != null && schema.length() > 0)
							rs = meta.getColumns(null, schema, str[i], null);
						else
							rs = meta.getColumns(null, null, str[i], null);
						printRS(schemaDlg.getCoulmnsTable(), "", rs, 0);
						rs.close();
						if (schema != null && schema.length() > 0)
							rs = meta.getPrimaryKeys(null, schema, str[i]);
						else
							rs = meta.getPrimaryKeys(null, null, str[i]);
						if (rs == null)
							throw new Exception("Failed to load metadata.");
						printRS(schemaDlg.getKeysTable(), "", rs, 0);
						rs.close();
						conn.close();
						conn = null;
					} catch (SQLException se) {
						se.printStackTrace();
						showError("SQL Error Code : " + se.getErrorCode()
								+ "\nMessage : " + se.getMessage());
					} catch (ClassNotFoundException ce) {
						showError("Failed to load the specified JDBC driver : "
								+ ce.getMessage()
								+ "\nCopy JDBC driver to <install-dir>/ext-jars folder.");
					} catch (Exception ee) {
						// e.printStackTrace();
						ExceptionDialog(ee);
						// showError(ee.toString());
					} finally {
						if (conn != null)
							try {
								conn.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					}
					// }
					schemaDlg.setTitle("Schema Explorer - " + str[i]);
					shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
				}
			}
		});

		schemaDlg.open();
	}

	/**
	 * Open DB Explorer settings dialog.
	 *
	 */
	protected void showSettings() {
		SettingsDialog settings = new SettingsDialog(shell);
		// settings.setTitle("DB Explorer Settings");
		settings.buildUI();

		settings.getRowsText().setText(maxRows + "");
		for (int i = 0; i < commonDrivers.size(); i++) {
			settings.getDriver().add(commonDrivers.get(i).toString());
		}
		settings.getDriver().select(commonDrivers.indexOf(driver));
		settings.getUrl().setText(url);
		settings.getUser().setText(user);
		settings.getPassword().setText(password);
		settings.getToken().setText(token);
		settings.getSchema().setText(schema);
		settings.open();

		if (settings.isOkClicked()) {

			maxRows = settings.getRowSize();

			if (settings.getDriverStr() != null
					&& settings.getDriverStr().length() > 0) {
				driver = settings.getDriverStr();
				if (!commonDrivers.contains(driver))
					commonDrivers.add(driver);
			}
			url = settings.getUrlStr();
			user = settings.getUserStr();
			password = settings.getPasswordStr();
			token = settings.getTokenStr();
			schema = settings.getSchemaStr();
			allTables.clear();
			SessionConnection sessConn = new SessionConnection();
			sessConn.driver = driver;
			sessConn.url = url;
			sessConn.user = user;
			sessConn.password = password;
			if (!sessConns.contains(sessConn)) {
				sessConns.add(sessConn);
				addSessionConn();
			}
			shell.setText("DB Explorer - " + url + ", User=" + user);

		}

	}

	/**
	 * Execute SQL query
	 *
	 */
	private void executeQuery() {

		Control control = getCurrentResultAreaControl();
		if (control != null) {
			runQuery(control, getCurrentSQL());
		}

	}

	private Control getCurrentResultAreaControl() {
		if (folder.getSelection() != null) {
			SashForm form = (SashForm) folder.getSelection().getControl();
			Control[] child = form.getChildren();
			return child[1];
			// return (Table) comp.getChildren()[0];

		}
		return null;
	}

	private SashForm getCurrentForm() {
		if (folder.getSelection() != null) {
			return (SashForm) folder.getSelection().getControl();
		}
		return null;
	}

	private String getCurrentSQL() {
		if (folder.getSelection() != null) {
			SashForm form = (SashForm) folder.getSelection().getControl();
			Control[] child = form.getChildren();
			return ((Text) child[0]).getText();

		}
		return "";
	}

	/*
	 * private Text getCurrentSQLText() { if(folder.getSelection()!=null) {
	 * SashForm form = (SashForm) folder.getSelection().getControl(); Control[]
	 * child = form.getChildren(); return ((Text) child[0]);
	 *  } return null; }
	 */
	private void closeTab() {

		if (folder.getSelection() != null) {
			folder.getSelection().dispose();
			tabCount--;
		}

	}

	public static Connection getConnection(boolean csv) throws Exception {

		Class.forName(driver);
		if ((user == null || user.length() == 0)
				&& (password == null || password.length() == 0)) {
			return DriverManager.getConnection(url);
		} else {
			return DriverManager.getConnection(url, user, password);
		}

	}

	/**
	 * Execute query.
	 * @param control
	 * @param sql
	 */
	private void runQuery(Control control, String sql) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
		int rows = 0;
		Composite comp = null;
		if (control instanceof Composite) {
			comp = (Composite) control;
		}
		if (sql != null && sql.length() > 0) {
			if (comp != null) {

				while (comp.getChildren().length > 0) {
					comp.getChildren()[comp.getChildren().length - 1].dispose();

				}
			}
			sql = removeComment(sql);

			if (sql.length() > 0 && !sql.trim().endsWith(token))
				sql = sql + token;
			Vector sqlList = new Vector();
			StringTokenizer stk = new StringTokenizer(sql, token);
			while (stk.hasMoreTokens()) {
				String t = stk.nextElement().toString();
				if (t.trim().length() > 0)
					sqlList.add(t);
			}

			for (int i = 0; i < sqlList.size(); i++) {
				sql = sqlList.get(i).toString();

				Connection conn = null;
				try {

					conn = getConnection(false);

					java.sql.Statement stmt = conn.createStatement();
					ResultSet rs = null;
					if (sql.trim().toUpperCase().startsWith("INSERT")
							|| sql.trim().toUpperCase().startsWith("UPDATE")
							|| sql.trim().toUpperCase().startsWith("DELETE")
							|| sql.trim().toUpperCase().startsWith("CREATE")
							|| sql.trim().toUpperCase().startsWith("ALTER")
							|| sql.trim().toUpperCase().startsWith("DROP")) {
						// conn.setAutoCommit(false);
						rows = stmt.executeUpdate(sql);
						// conn.commit();
						// conn.setAutoCommit(true);
						Table table = null;
						if (control instanceof Table) {
							table = (Table) control;
						} else {
							table = addTable((Composite) control, i, sqlList
									.size());

						}
						showMessage(table, rows
								+ " row(s) updated for query \" " + sql + " \"");
					} else {

						if (!sql.trim().toUpperCase().startsWith("SELECT")) {
							if (schema != null && schema.length() > 0) {
								sql = "Select * from " + schema + "." + sql;
							} else {
								sql = "Select * from " + sql;
							}
						}
						long starttime = System.currentTimeMillis();
						rs = stmt.executeQuery(sql);
						Table table = null;
						if (control instanceof Table) {
							table = (Table) control;
						} else {
							table = addTable((Composite) control, i, sqlList
									.size());

						}
						printRS(table, sql, rs, System.currentTimeMillis()
								- starttime);
						// getCurrentStatus().setText(sql);
						// table.pack();

					}
					conn.close();
					conn = null;
					if (!queries.contains(sql))
						queries.add(sql);

				} catch (SQLException se) {
					showError("SQL Error Code : " + se.getErrorCode()
							+ "\nMessage : " + se.getMessage());
				} catch (ClassNotFoundException ce) {
					showError("Failed to load the specified JDBC driver : "
							+ ce.getMessage()
							+ "\nCopy JDBC driver to <install-dir>/ext-jars folder.");
				} catch (Exception e) {
					e.printStackTrace();
					// ExceptionDialog(e);
					showError(e.toString());
				} finally {
					try {
						if (conn != null)
							conn.close();
					} catch (SQLException e) {
						ExceptionDialog(e);
					}
				}
			}
			if (comp != null) {

				int k = 0;
				while (k < comp.getChildren().length) {
					comp.getChildren()[k].pack();
					k++;

				}

				comp.layout();
				getCurrentForm().layout();
				shell.layout();

			}
		} else {
			showMessage("Please enter a SQL.");

		}
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
	}

	private void showMessage(Table table, String message) {
		while (table != null && table.getColumnCount() > 0) {
			int lastOne = table.getColumnCount() - 1;
			table.getColumn(lastOne).dispose();
		}
		table.removeAll();
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("SQL Result");
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(message);
		table.getColumn(0).pack();
		table.addListener(SWT.MouseDoubleClick, new SelectableTableListener(
				table));
		table.addKeyListener(new KeyboardCopyTableListener(table));

	}

	private static void ExceptionDialog(Exception e) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
		MessageBox box = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR
				| SWT.APPLICATION_MODAL);
		box.setText("DB Explorer-Error");
		StringWriter stacktrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stacktrace));
		box.setMessage(stacktrace.toString().replace('\r', ' ').replace('\t',
				' '));
		box.open();

	}

	private void showMessage(String message) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
		MessageBox box = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION
				| SWT.APPLICATION_MODAL);
		box.setText("DB Explorer-Message");
		box.setMessage(message);
		box.open();
	}

	private static void showError(String message) {
		shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
		MessageBox box = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR
				| SWT.APPLICATION_MODAL);
		box.setText("DB Explorer-Error");
		box.setMessage(message);
		box.open();
	}

	private void addTab() {
		tabCount++;
		CTabItem item = new CTabItem(folder, SWT.CLOSE | SWT.V_SCROLL
				| SWT.H_SCROLL);
		item.setText("DB Tab " + tabCount);
		item.setFont(new Font(display, "Verdana", 10, SWT.BOLD));

		SashForm form = new SashForm(folder, SWT.VERTICAL);
		form.setLayout(new FillLayout());
		Text text = new Text(form, SWT.CHECK | SWT.V_SCROLL | SWT.WRAP);
		text.setText("");
		text.setToolTipText("Enter SQL here.");
		text.setFont(new Font(display, "Verdana", 10, SWT.NORMAL));

		Composite bottom = new Composite(form, SWT.None);
		bottom.setLayout(new GridLayout());

		item.setControl(form);
		form.setWeights(new int[] { 15, 85 });
		folder.setSelection(item);
		text.setFocus();

	}

	private Table addTable(Composite control, int i, int size) {

		SashForm form = null;
		if (i == 0) {
			form = new SashForm(control, SWT.VERTICAL);
			form.setLayout(new GridLayout(1, false));
			form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} else {
			if (control.getChildren() != null
					&& control.getChildren().length > 0) {
				form = (SashForm) control.getChildren()[0];
			} else {
				form = new SashForm(control, SWT.VERTICAL);
				form.setLayout(new GridLayout(1, false));
				form
						.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
								true));
			}
		}

		Table table = new Table(form, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.MULTI | SWT.FULL_SELECTION);
		GridData g = new GridData(SWT.FILL, SWT.FILL, true, true);
		g.minimumHeight = 25;
		table.setLayoutData(g);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setToolTipText("SQL result.");
		return table;

	}

	/**
	 * Clear result table and add query result
	 * @param table
	 * @param sql
	 * @param rs
	 * @param time
	 * @throws SQLException
	 */
	private void printRS(final Table table, String sql, ResultSet rs, long time)
			throws SQLException {

		while (table.getColumnCount() > 0) {
			int lastOne = table.getColumnCount() - 1;
			table.getColumn(lastOne).dispose();
		}
		table.removeAll();

		Font f = new Font(display, "Verdana", 9, SWT.NORMAL);
		if (rs == null) {
			showError("ResultSet is null.");
			return;
		}
		ResultSetMetaData rsm = rs.getMetaData();
		int colCount = rsm.getColumnCount();
		int count = 0;
		for (int i = 1; i <= colCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);

			column
					.setText(rsm.getColumnName(i)
							+ " ( "
							+ rsm.getColumnTypeName(i).toUpperCase()
							+ ":"
							+ rsm.getPrecision(i)
							+ ","
							+ rsm.getScale(i)
							+ ") "
							+ (rsm.isNullable(i) == ResultSetMetaData.columnNullable ? "NULL"
									: "NOT NULL"));

		}

		while (rs.next()) {

			if (count == maxRows && maxRows != -1)
				break;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setFont(f);
			for (int i = 1; i <= colCount; i++) {
				String data = rs.getString(i);
				if (data == null)
					data = "";
				item.setText(i - 1, data);

			}

			count++;
		}

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(count + " row(s) in " + (float) time / 1000 + " secs.");

		for (int i = 0; i < colCount; i++) {
			table.getColumn(i).pack();
		}

		table.addListener(SWT.MouseDoubleClick, new SelectableTableListener(
				table));
		table.addKeyListener(new KeyboardCopyTableListener(table));

	}

	/**
	 * Add tray icon
	 *
	 */
	private void setTray() {
		final Tray tray = display.getSystemTray();
		if (tray == null) {
			showMessage("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("DB Explorer");
			item.addListener(SWT.Show, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("show");
				}
			});
			item.addListener(SWT.Hide, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("hide");
				}
			});
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					shell.setVisible(!shell.isVisible());
					if (shell.isVisible()) {
						shell.setActive();

					}

				}
			});
			item.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("default selection");
				}
			});

			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem mi = new MenuItem(menu, SWT.PUSH);
			mi.setText("Exit");
			mi.setText("E&xit\tALT+X");
			// mi.setImage(new Image(display,
			// display.getClass().getResourceAsStream("/icon/exit.gif")));
			mi.setAccelerator(SWT.ALT + 'X');
			mi.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					shell.dispose();
					display.dispose();
				}
			});

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);

				}
			});

			item.setImage(new Image(display, display.getClass()
					.getResourceAsStream("/icon/icon.jpg")));
		}
	}

	/**
	 * Remove comment from SQL
	 * @param str
	 * @return
	 */
	String removeComment(String str) {
		String find = "/*", replace = "", end = "*/";

		StringBuffer sb = new StringBuffer(str.length());
		int pos = 0;
		int lastPos = 0;

		while (pos >= 0) {
			pos = str.indexOf(find, lastPos);
			if (pos >= 0) {
				sb.append(str.substring(lastPos, pos));
				sb.append(replace);
				pos = str.indexOf(end, lastPos);
			} else {
				sb.append(str.substring(lastPos));
			}
			lastPos = pos + find.length();
		}

		return sb.toString();
	}
	/**
	 * Class to track multiple connection.
	 * @author Sheel
	 *
	 */

	class SessionConnection {
		protected String id;

		protected String driver;

		protected String url;

		protected String user;

		protected String password;

		public SessionConnection() {
			id = "Conn" + connCounter;
			connCounter++;
		}

		public boolean equals(Object obj) {

			if (obj != null && obj instanceof SessionConnection) {
				if (((SessionConnection) obj).url.equals(this.url)
						&& ((SessionConnection) obj).user.equals(this.user))
					return true;
				else
					return false;
			} else {
				return false;
			}
		}

		public int hascode() {
			return url.hashCode() + user.hashCode();
		}

	}

}
