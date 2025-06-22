import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task t = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("isDone")
                );
                tasks.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }
    public static void addTask(Task task){
        String sql = "INSERT INTO tasks (name, description, isDone) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isDone());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateTask(Task task){
        String sql = "UPDATE tasks SET name = ?, description = ?, isDone = ? WHERE id = ?";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1,task.getName());
            pstmt.setString(2,task.getDescription());
            pstmt.setBoolean(3, task.isDone());
            pstmt.setInt(4, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void removeTask(Task task){
        String sql = "DELETE FROM tasks WHERE id = ?";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

