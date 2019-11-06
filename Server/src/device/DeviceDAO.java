package device;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import db.DatabaseUtil;
import java.sql.Connection;

public class DeviceDAO {

	public String getDate() {
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
		return ""; // 데이터베이스 오류
	}

	public static String devlogin(String deviceID) { // 디바이스 아이디 찾기
		String SQL = "SELECT deviceid FROM DEVICE WHERE deviceid  = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, deviceID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(deviceID)) {
					return deviceID; // 로그인 성공
				} else {
					return "0"; // 비밀번호 틀림
				}
			}
			return "fail"; // 아이디 없음
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "error"; // 데이터베이스 오류
	}

	public DeviceDTO getdevice(String date) {
		String SQL = "SELECT * FROM DEVICE WHERE DATE(Date) = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				DeviceDTO device = new DeviceDTO();
				device.setDeviceID(rs.getString(1));
				device.setMlTime(rs.getInt(2));
				device.setUseruseTime(rs.getInt(3));
				device.setTotalTime(rs.getInt(4));
				device.setDate(rs.getString(5));
				return device;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
