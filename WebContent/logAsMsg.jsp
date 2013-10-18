<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<body> <%
	
	String to = request.getParameter( "to" );
	String from = request.getParameter( "from" );
	String displayFrom = request.getParameter( "displayFrom" );
	String body = request.getParameter( "body" );
	String type = "MSGTO";	
	String uid = request.getParameter( "uid" );
	String dummy = request.getParameter( "dummy" );
	
	if( dummy == null ){
		dummy = "0";
	}
	
	// Send dummy text.
	if( dummy.equals( "1" ) ) 
		out.write( "1234" );
	else if( !uid.equals( "undefined" ) ) {
	/**
	 * Connect to the database and retrieve the PIN.
	 * @param uid - The the user id (user name) of the user.
	 * @return PIN or null
	**/
		String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
		String DATABASE_URL = "localhost";
		String DATABASE_NAME = "iscoop";
		
		String jdbcURL = "jdbc:mysql://" + DATABASE_URL + "/" + DATABASE_NAME;
		
		String QUERY = "INSERT INTO is_messages SET ";
		QUERY += "FROM = '" + from + "', ";
		QUERY += "TO = '" + to + "', ";
		QUERY += "displayFrom = '" + displayFrom + "', ";
		QUERY += "body = '" + body + "', ";
		QUERY += "type = '" + type + "';";
		
		Connection conn;
		ResultSet rs;
		Statement stmt;
		
		try {
			Class.forName( DATABASE_DRIVER ).newInstance();
			
			conn = DriverManager.getConnection( jdbcURL, "root", "spinsci" );
			stmt = conn.createStatement();
			rs = stmt.executeQuery( QUERY );
			
		} catch( SQLException se ) {
			while( se != null ) {
				System.err.println( "State: " + se.getSQLState() );
				System.err.println( "Message: " + se.getMessage() );
				System.err.println( "Error: " + se.getErrorCode() );
				se = se.getNextException();
			}
		} catch( ClassNotFoundException ce ) {
			System.out.println( "Could not find driver." );
			System.out.println( ce.getMessage() );
		} catch( InstantiationException ie ) {
			System.out.println( "Could not instantiate driver." );
		} catch( IllegalAccessException iae ) {
			System.out.println( "Prevented from accessing driver." );
		} finally {
			try { rs.close(); } catch( SQLException se ){}
			try { stmt.close();	} catch( SQLException se ){}
			try { conn.close();	} catch( SQLException se ){}
		}
	}
	else out.write( "-1" ); 

%> </body>
</html>