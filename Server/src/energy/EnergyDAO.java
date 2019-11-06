package energy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DatabaseUtil;

public class EnergyDAO {
	public String getEDate() {
		String SQL = "SELECT DATE()";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public EnergyDTO getenergy(String date) {
		String SQL = "SELECT * FROM ENERGY WHERE DATE(Edate) = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				EnergyDTO e = new EnergyDTO();

				e.setEfficiency(rs.getInt(1));
				e.setEdate(rs.getString(2));

				return e;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
