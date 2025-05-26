package com.example.bankSystem.repository;
import com.example.bankSystem.DAO.DatabaseDAO;
import com.example.bankSystem.model.*;
import com.example.bankSystem.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepo
{
    @Autowired
    private MailSender mailSender;

    @Autowired
    private DatabaseDAO databaseDAO;

    //----------Adding new user
    public void addNewUser(String email, HashMap<String, User> map)
    {
        User obj = map.get(email);
        System.out.println(obj.getEmail());
        System.out.println(obj.getPassword());
        System.out.println(obj.getPan());
        System.out.println(obj.getFullname());
        System.out.println(obj.getMobile());
        System.out.println("Connected to the database.");
        // Perform database operations here...
        String sql = "INSERT INTO users (fullname, mobile, aadhar, pan_id, address, email, password, status, balance, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getFullname());
            pstmt.setString(2, obj.getMobile());
            pstmt.setString(3, obj.getAadhar());
            pstmt.setString(4, obj.getPan());
            pstmt.setString(5, obj.getAddress());
            pstmt.setString(6, email);
            pstmt.setString(7, obj.getPassword());
            pstmt.setString(8, "Pending");
            pstmt.setString(9, "0");
            pstmt.setString(10, "user");

            // Execute the insert operation
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ User inserted successfully!");
            } else {
                System.out.println("❌ Failed to insert user.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL error if insertion fails
        }
    }

    //----------checking an existing user ?
    public boolean isUserAlreadyExist(String aadhar) {
        String query = "SELECT COUNT(*) FROM users WHERE aadhar = ?";
        try (Connection conn = databaseDAO.getConnection();  // Assuming databaseDAO.getConnection() establishes a database connection
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, aadhar);  // Set the Aadhar number in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);  // Get the count of matching records
                    return count > 0;  // If count is greater than 0, Aadhar exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions, possibly log them
        }
        return false;  // Return false if Aadhar number doesn't exist or on error
    }

    //----------------verify user password
    public boolean verifyPassword(String aadhar, String password)
    {
        String query = "SELECT password FROM users WHERE aadhar = ?";
        try (Connection conn = databaseDAO.getConnection();  // Assuming databaseDAO.getConnection() establishes a database connection
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, aadhar);  // Set the Aadhar number in the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");  // Retrieve the stored password
                    return storedPassword.equals(password);  // Compare with provided password
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions, possibly log them
        }
        return false;  // Return false if Aadhar number doesn't exist or on error
    }


    public boolean verifyPaymentPassword(String aadhar, String password)
    {
        String query = "SELECT payment_password FROM users WHERE aadhar = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, aadhar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("payment_password");
                    return storedPassword.equals(password);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    //-----------------getting user status
    public String getUserStatus(String aadhar) {
        String query = "SELECT status FROM users WHERE aadhar = ?";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, aadhar);  // Set the Aadhar number in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");  // Retrieve and return the status
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions, possibly log them
        }

        return null;  // Return null if Aadhar number doesn't exist or on error
    }


    public boolean updateProfilePicture(String aadhar, InputStream imageStream) {
        String sql = "UPDATE users SET profile_pic = ? WHERE aadhar = ?";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBlob(1, imageStream);  // Set image stream as BLOB
            ps.setString(2, aadhar);     // Match using aadhar (userId)

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //----------------------------------------------------fetching porifle pic to frontend
    public UserProfileDTO getUserProfile(String aadhar) {
        String sql = "SELECT fullname, profile_pic FROM users WHERE aadhar = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aadhar);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserProfileDTO dto = new UserProfileDTO();
                dto.setFullname(rs.getString("fullname"));

                byte[] imageBytes = rs.getBytes("profile_pic");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    dto.setProfilePicBase64(base64Image);
                }

                return dto;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //----------------------------------------------------storing action taken image to db
    public boolean insertAction(String aadhar, String action, String timestamp, String date, byte[] image) {
        String sql = "INSERT INTO action_taken (aadhar, action, timestamp, date, bolo_image) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aadhar);
            stmt.setString(2, action);
            stmt.setString(3, timestamp);
            stmt.setString(4, date);
            stmt.setBytes(5, image);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    //----------------------------------------------------dashboard data to frontend
    public UserDashboardDTO getUserDashboardData(String userId) {
        String query = "SELECT fullname, mobile, aadhar, pan_id, address, email, status, balance FROM users WHERE aadhar = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserDashboardDTO dto = new UserDashboardDTO();
                dto.setFullname(rs.getString("fullname"));
                dto.setMobile(rs.getString("mobile"));
                dto.setAadhar(rs.getString("aadhar"));
                dto.setPan_id(rs.getString("pan_id"));
                dto.setAddress(rs.getString("address"));
                dto.setEmail(rs.getString("email"));
                dto.setStatus(rs.getString("status"));
                dto.setBalance(rs.getString("balance"));
                return dto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //---------------------------------------------------- fetching transaction history to frontend
    public List<Transaction> getTransactionsByUserId(String userId) {
        List<Transaction> transactions = new ArrayList<>();

        String query = "SELECT * FROM transaction_history " +
                "WHERE Sender_id = ? OR Receiver_id = ? " +
                "ORDER BY time_stamp DESC";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, userId);
            stmt.setString(2, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setUtrId(rs.getString("UTR_id"));
                tx.setSenderId(rs.getString("Sender_id"));
                tx.setReceiverId(rs.getString("Receiver_id"));
                tx.setCredited(rs.getString("Credited"));
                tx.setDebited(rs.getString("Debited"));
                tx.setTimeStamp(rs.getString("time_stamp"));
                tx.setPaymentStatus(rs.getString("payment_status"));
                tx.setBankBalance(rs.getString("bank_balance"));
                transactions.add(tx);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return transactions;
    }


    public boolean isCheckingtheOldPassword(String userId,String oldpassword)
    {
        String query = "select password from users where aadhar = ?";

        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                String storedpassword = rs.getString("password");
                if(storedpassword.equals(oldpassword))
                {
                    return true;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getUserStatusById(String userId)
    {
        String status = null;
        String sql = "SELECT status FROM users WHERE aadhar = ?";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                status = rs.getString("status");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status; // could be "active", "inactive", or null if user not found
    }

    public void updateUserStatus(String userId, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE aadhar = ?";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User status updated successfully.");
            } else {
                System.out.println("No user found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String fetchingemailByUserId(String userId)
    {
        String query = "select email from users where aadhar = ?";

        try(Connection conn = databaseDAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return rs.getString("email");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return "not found";
    }



    public void updatingUserPassword(String userId,String password)
    {
        String query ="UPDATE users SET password = ? WHERE aadhar = ?";
        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(2,userId);
            ps.setString(1,password);

            int affectedRow = ps.executeUpdate();
            if(affectedRow>0)
            {
                System.out.println("db updated");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String isCheckingtheOldMobile(String userId,String oldMobile)
    {
        String query = "select mobile from users where aadhar = ?";

        try(Connection conn = databaseDAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return rs.getString("mobile");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return "Not found";
    }

    public void updatingUserMobile(String userId,String mobile)
    {
        String query ="UPDATE users SET mobile = ? WHERE aadhar = ?";
        try(Connection conn = databaseDAO.getConnection();
            PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(2,userId);
            ps.setString(1,mobile);

            System.out.println(userId +" "+mobile);
            int affectedRow = ps.executeUpdate();
            if(affectedRow>0)
            {
                System.out.println("mobile db updated");
            }
            else
            {
                System.out.println("not updated");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateEmailAddress(String userId,String email)
    {
        String query = "UPDATE users SET email = ? WHERE  aadhar = ?";
        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,email);
            ps.setString(2,userId);

            int affected = ps.executeUpdate();
            if(affected>0)
            {
                System.out.println("Email update success full");
            }
            else
            {
                System.out.println("Not updated");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateHomeAddressRepo(String userId,String address)
    {
        String query = "UPDATE users SET address = ? WHERE aadhar = ?";
        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,address);
            ps.setString(2,userId);

            int affected = ps.executeUpdate();
            if(affected>0)
            {
                System.out.println("address db updated");
            }
            else
            {
                System.out.println("address db faild");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


    public boolean UpdatingActionTakenRepo(String userId,String action)
    {
        String query = "UPDATE users SET status = ? WHERE  aadhar =?";
        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,action);
            ps.setString(2,userId);
            int affected = ps.executeUpdate();
            if(affected>0)
            {
                System.out.println("Action db updated");
                return true;
            }
            else
            {
                System.out.println("Action db failed");
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return true;
    }


    public boolean DeletingtheUserAccountRepo(String userId)
    {
        String query = "DELETE FROM users WHERE aadhar = ?";
        try(Connection conn = databaseDAO.getConnection();
        PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1,userId);
            int affedted = ps.executeUpdate();
            if(affedted>0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updatingPaymentPasswordRepo(String user,String password)
    {
        String query ="UPDATE users SET payment_password = ? WHERE aadhar = ?";
        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setString(1,password);
            ps.setString(2,user);
            int affrectedRow = ps.executeUpdate();
            if(affrectedRow>0)
            {
                System.out.println("Payment password dome");
            }
            else
            {
                System.out.println("Failed payment password");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public List<String> AutoApprovalRepo()
    {
        List<String> list = new ArrayList<>();
        String query = "SELECT * FROM users WHERE status = 'Pending'";
        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                String aadahr =  rs.getString("aadhar");
                String email = rs.getString("email");
                String fullname = rs.getString("fullname");
                AutoApproval autoApproval = new AutoApproval();
                autoApproval.setEmail(email);
                autoApproval.setFullName(fullname);
                autoApproval.setAachar(aadahr);
                mailSender.AutoApprovalEmailSending(fullname,email,aadahr);
                String query2 = "UPDATE users SET status = 'Active' WHERE aadhar  = ?";
                try(PreparedStatement pstm = connection.prepareStatement(query2))
                {
                    pstm.setString(1,aadahr);
                    int affectedRow = pstm.executeUpdate();
                    if(affectedRow>0)
                    {
                        System.out.println("User Activated Successfully");
                        list.add(aadahr);
                    }
                    else
                    {
                        System.out.println("Failed to activate users");
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return list;
    }


    //---------------botification -
    public List<NotificationDTO> getUserNotifications(String userId)
    {
        List<NotificationDTO> notifications = new ArrayList<>();

        String sql = "SELECT title, message, status, timestamp FROM notifications WHERE userId = ? ORDER BY timestamp DESC";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NotificationDTO dto = new NotificationDTO();
                dto.setTitle(rs.getString("title"));
                dto.setMessage(rs.getString("message"));
                dto.setStatus(rs.getString("status"));
                dto.setTimestamp(rs.getTimestamp("timestamp").toString());
                notifications.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }
}
