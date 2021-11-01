package fr.skyreth.doorjustice;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Connexion 
{
	private String DBPath;
	private java.sql.Connection connexion = null;
	ResultSet rs = null;

	public Connexion(String dBPath) {
		DBPath = dBPath;
	}

	private java.sql.Connection getConnexion() {
		try {
			if (connexion == null || connexion.isClosed())
				connexion = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			else
				return connexion;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connexion;
	}

	public void connect() {
		try 
		{
			Class.forName("org.sqlite.JDBC");
			connexion = this.getConnexion();
			connexion.createStatement();
			System.out.println("Connexion a " + DBPath + " avec succès");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("Erreur de connecxion");
		} finally {
			try {
				if (connexion != null) {
					connexion.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void insert3(String sql, String value1, String value2, String value3) {
		try {
			java.sql.Connection conn = getConnexion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, value1);
			pstmt.setString(2, value2);
			pstmt.setString(3, value3);
			pstmt.executeUpdate();
			close2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insert2(String sql, String value1, String value2) {
		try {
			java.sql.Connection conn = getConnexion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, value1);
			pstmt.setString(2, value2);
			pstmt.executeUpdate();
			close2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insert(String sql, String value) {
		try {
			java.sql.Connection conn = getConnexion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, value);
			pstmt.executeUpdate();
			close2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createNewTable(String tablename, String tablecontent) {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + tablename + " (" + tablecontent + ")";

			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTable(String tablename) {
		try {
			String sql = "DROP TABLE " + tablename;

			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String selectAll3(String table, String selection, String selection2, String selection3) {
		String sql = "SELECT * FROM " + table;

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String reponse = rs.getString(selection) + rs.getString(selection2) + rs.getString(selection3);
				close2();
				return reponse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String selectWhere2(String table, String argname, String comparator, String argtoget, String argtoget2) {
		String sql = "SELECT * FROM " + table + " WHERE " + argname + " = '" + comparator + "'";

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String reponse = rs.getString(argtoget);
				String reponse2 = rs.getString(argtoget2);
				close2();
				return reponse + "-" + reponse2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "nodata";
	}
	
	public String selectWhere2condition(String table, String argname, String comparator, String argname2, String comparator2, String argtoget) {
		String sql = "SELECT * FROM " + table + " WHERE (" + argname + " = '" + comparator + "'" + " AND " +argname2+" = '"+ comparator2 + "')";

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String reponse = rs.getString(argtoget);
				close2();
				return reponse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "nodata";
	}
	
	public void delete(String table, String argname, String comparator) {
		String sql = "DELETE FROM " + table + " WHERE " + argname + " = '" + comparator + "'";

		try {
			java.sql.Connection conn = this.getConnexion();
		    PreparedStatement pstmt = conn.prepareStatement(sql);
		    pstmt.executeUpdate();
		    close2();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String selectWhere(String table, String argname, String comparator, String argtoget) {
		String sql = "SELECT * FROM " + table + " WHERE " + argname + " = '" + comparator + "'";

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String reponse = rs.getString(argtoget);
				close2();
				return reponse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "nodata";
	}
	
	public String getAllCells(String table, String elements, String argname, String comparator)
	{
		String sql = "SELECT * FROM " + table + " WHERE "+argname+" = '"+comparator+"'";
		String reponse = "";
		
		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
	
			while(rs.next()) 
			{
				reponse += rs.getString(elements)+",";
			}
			
			close2();
		} 
		catch (SQLException e) {
            e.printStackTrace();
        }
			
		return reponse;
	}
	
	public String getAllCellsWA(String table, String elements)
	{
		String sql = "SELECT * FROM " + table;
		String reponse = "";
		
		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
	
			while(rs.next()) 
			{
				reponse += rs.getString(elements)+",";
			}
			
			close2();
		} 
		catch (SQLException e) {
            e.printStackTrace();
        }
			
		return reponse;
	}
	
	public String select(String table, String argtoget) {
		String sql = "SELECT ? FROM " + table;

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String reponse = rs.getString(argtoget);
				close2();
				return reponse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "nodata";
	}
	
	public String select2(String table, String argtoget, String secondargtoget) {
		String sql = "SELECT ?,? FROM " + table;

		try {
			java.sql.Connection conn = this.getConnexion();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);

			while (rs.next()) {
				String reponse = rs.getString(argtoget);
				String reponse2 = rs.getString(secondargtoget);
				close2();
				return reponse+"-"+reponse2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "nodata";
	}

	public void replaceDataWhere(String table, String subtable, String data, String newdata, String condition) {
		String sql = "UPDATE " + table + " SET " + subtable + " = ? WHERE " + condition + " = ?";

		try {
			java.sql.Connection conn = this.getConnexion();
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, newdata);
			pstmt.setString(2, data);
			pstmt.executeUpdate();
			close2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceData(String table, String subtable, String newdata) {
		String sql = "UPDATE " + table + " SET " + subtable + " = ?";

		try {
			java.sql.Connection conn = this.getConnexion();
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(2, newdata);
			pstmt.executeUpdate();
			close2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(Logger log) {
		try {
			java.sql.Connection conn = this.getConnexion();

			if (conn != null && !conn.isClosed()) {
				conn.close();
				log.info("[SkyUtils]: Data base closed !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close2() {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
