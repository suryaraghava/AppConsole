<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%
String uid = request.getParameter( "uid" );
String dummy = request.getParameter( "dummy" );

if( dummy == null )
	dummy = "0";

// Send dummy text.
if( dummy.equals( "1" ) )
	out.write("1234");
else if ( !uid.equals( "undefined" ) ){
/**
 * Connect to the database and retrieve the PIN.
 * @param uid The the user id (user name) of the user.
 * @return PIN or null
 */
	String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	String DATABASE_URL = "localhost";
	String DATABASE_NAME = "iscoop";
	String QUERY = "SELECT webportal.users.pin FROM is_accounts LEFT JOIN webportal.users ON is_accounts.puser = webportal.users.id WHERE uid='" + uid + "';";

	String jdbcURL = "jdbc:mysql://" + DATABASE_URL + "/" + DATABASE_NAME;
	
	Connection conn;
	ResultSet rs;
	Statement stmt;
	
	try {
		Class.forName( DATABASE_DRIVER ).newInstance();
		
		conn = DriverManager.getConnection( jdbcURL, "root", "spinsci" );
		stmt = conn.createStatement();
		
		rs = stmt.executeQuery( QUERY );
		rs.first();
		String pin = rs.getString( "pin" );
		out.write( pin );
		
	} catch( SQLException se ) {
		while( se != null ) {
			System.err.println( "State: " + se.getSQLState() );
			System.err.println( "Message: " + se.getMessage() );
			System.err.println( "Error: " + se.getErrorCode() );
			se = se.getNextException();
			out.write( "-1" );
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
%>