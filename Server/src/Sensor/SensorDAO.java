package Sensor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DatabaseUtil;

public class SensorDAO {
	public String getSDate() {
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

	public SensorDTO getsensor(String date) {
		String SQL = "SELECT * FROM SENSOR WHERE DATE(SensorDate) = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				SensorDTO sensor = new SensorDTO();
				sensor.setTemperature(rs.getInt(1));
				sensor.setHumidity(rs.getInt(2));
				sensor.setSDate(rs.getString(3));
				return sensor;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}