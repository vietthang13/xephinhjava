package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScoreDAO {
	/**
	 * Insert score to table
	 * 
	 * @param name  name of player
	 * @param score score of player
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void insertScore(String name, int score) throws SQLException, ClassNotFoundException {
		try {
			Connection conn = ConnectionUtils.getMyConnection();

			String sql = "Insert into Score(Name, Score)" + " values(?,?)";

			PreparedStatement statement = conn.prepareStatement(sql);

			// add score to table
			statement.setString(1, name);
			statement.setInt(2, score);
			statement.executeUpdate();

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get a list of score from table
	 * 
	 * @param name
	 * @param score
	 * @return ArrayList<Score>
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<ScoreBean> selectScore() throws SQLException, ClassNotFoundException {
		ArrayList<ScoreBean> listScore = new ArrayList<ScoreBean>();
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			ScoreBean scr = null;
			String sql = "SELECT Id,Name,Score FROM Score ";

			PreparedStatement statement = conn.prepareStatement(sql);

			// add score to table
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				scr = new ScoreBean();
				scr.setId(rs.getInt(1));
				scr.setName(rs.getString(2));
				scr.setScore(rs.getInt(3));
				listScore.add(scr);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listScore;
	}
}
