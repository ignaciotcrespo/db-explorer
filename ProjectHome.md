**DB Explorer** is an easy-to-use application that lets you manage any JDBC (Java Database Connectivity) compliant database. It provides connectivity to wide-range of SQL databases through their JDBC drivers. This makes it completely different from other single database centric applications.
Features like simple tabbed-interface to run SQLs query, Schema-Browser, multiple database connections in a session and powerful CSV export/import makes it perfect tool to manage any database.
### Features ###
  * Support multiple queries per tab. Use Separator token to separate multiple SQLs.
  * Limit number of rows in query result. Default is 50 rows.
  * Support comments in SQL. Use /"**" "**"/ to comment any part of SQL.
  * Powerful Database Schema Explorer.
  * Just enter 

&lt;table-name&gt;

 to execute simple SELECT query.
  * View previously Executed SQLs in History Dialog.
  * Tray hide/unhide support.
  * Export query result to CSV file.
  * Import CSV file.
  * Manage connections to multiple database in single session.

### Usage ###
Support any Database with JDBC driver. For non-executable(EXE) version, Add JDBC driver file to CLASSPATH or edit run.bat.

In Settings Window,
  * Select JDBC driver (com.mysql.jdbc.Driver).
  * Enter Database URL (for example, jdbc:mysql://localhost:3306/test)
  * Enter Database User and Password (if required).
  * Hit Ok button and you are ready to go.
You can change connection anytime by using Settings Window. Each Connection information(same or different database) will be maintained, so you can switch to any connection in active tab.
DB Explorer does not need any JDBC driver file to connect to ODBC connection.

The latest release version is 1.2.