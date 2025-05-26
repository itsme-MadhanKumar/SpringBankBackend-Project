package com.example.bankSystem.repository;
import com.example.bankSystem.DAO.DatabaseDAO;
import com.example.bankSystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class LoanRepo
{
    @Autowired
    private DatabaseDAO databaseDAO;

    public boolean savePgLoan(String applicationId, String userId, String name, String age, String dob, String tenthPercentage, String twelfthPercentage,String cgpa, String universityName, String courseName, String courseDuration, String counselingCode, String aadhaarCard, String panCard, String fatherIncome,  String loanAmount) throws SQLException {
        String sql = "INSERT INTO pg_loan_applications (" +
                "application_id, user_id, name, age, dob, tenth_percentage, twelfth_percentage,final_cgpa, " +
                "university_name, course_name, course_duration, counseling_code, aadhaar_card, pan_card, " +
                "father_income, loan_amount,status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, applicationId);
            stmt.setString(2, userId);
            stmt.setString(3, name);
            stmt.setString(4, age);
            stmt.setString(5, dob);
            stmt.setString(6, tenthPercentage);
            stmt.setString(7, twelfthPercentage);
            stmt.setString(8,cgpa);
            stmt.setString(9, universityName);
            stmt.setString(10, courseName);
            stmt.setString(11, courseDuration);
            stmt.setString(12, counselingCode);
            stmt.setString(13, aadhaarCard);
            stmt.setString(14, panCard);
            stmt.setString(15, fatherIncome);
//            stmt.setString(15, collateralPath);
            stmt.setString(16, loanAmount);
            stmt.setString(17,"Processing");

            int affected =stmt.executeUpdate();
            if(affected>0)
            {
                System.out.println("Pg db updated");
                return true;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }



    public void saveLoanApplication(UGLoanApplicationRequest request, String applicationId, String filePathsCsv) throws SQLException {
        String sql = "INSERT INTO ug_loan_applications (" +
                "application_id, user_id, name, age, dob, tenth_percentage, twelfth_percentage, " +
                "university_name, course_name, course_duration, counseling_code, aadhaar_card, pan_card, " +
                "father_income, collateral_documents_path, loan_amount,status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, applicationId);
            stmt.setString(2, request.getAadhaarCard());
            stmt.setString(3, request.getName());
            stmt.setString(4, request.getAge());
            stmt.setDate(5, Date.valueOf(request.getDob()));
            stmt.setString(6, request.getTenthPercentage());
            stmt.setString(7, request.getTwelfthPercentage());
            stmt.setString(8, request.getUniversityName());
            stmt.setString(9, request.getCourseName());
            stmt.setString(10, request.getCourseDuration());
            stmt.setString(11, request.getCounselingCode());
            stmt.setString(12, request.getAadhaarCard());
            stmt.setString(13, request.getPanCard());
            stmt.setString(14, request.getFatherIncome());
            stmt.setString(15, filePathsCsv);
            stmt.setString(16, request.getLoanAmount());
            stmt.setString(17, "Processing");
            int affected = stmt.executeUpdate();
            if(affected>0)
            {
                System.out.println("Ug Db updated");

            }
        }
    }

    public boolean saveLoanApplicationAbroad(AbroadLoanRequest application,String application_id)
    {
        String query = "INSERT INTO abroad_loan_applications (application_id, user_id, name, father_name, mother_name, community, " +
                "age, dob, tenth_percentage, twelfth_percentage, applying_for, university_name, course_name, " +
                "course_duration, aadhaar_card, pan_card, father_income, collateral_documents_path, travel_expenses, loan_amount,status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setString(1, application_id);
            ps.setString(2, application.getAadhaarCard());
            ps.setString(3, application.getName());
            ps.setString(4, application.getFatherName());
            ps.setString(5, application.getMotherName());
            ps.setString(6, application.getCommunity());
            ps.setString(7, application.getAge());
            ps.setString(8, application.getDob());
            ps.setString(9, application.getTenthPercentage());
            ps.setString(10, application.getTwelfthPercentage());
            ps.setString(11, application.getApplyingFor());
            ps.setString(12, application.getUniversityName());
            ps.setString(13, application.getCourseName());
            ps.setString(14, application.getCourseDuration());
            ps.setString(15, application.getAadhaarCard());
            ps.setString(16, application.getPanCard());
            ps.setString(17, application.getFatherIncome());
            ps.setString(18, application.getCollateralDocumentsPath());
            ps.setString(19, application.getTravelExpenses());
            ps.setString(20, application.getLoanAmount());
            ps.setString(21,"Processing");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


     //==============================================Display loan status in frontend
     public List<LoanStatusDTO> fetchUGLoanStatus(String userId)
     {
         System.out.println("UG FETCHED");
         String query = "SELECT application_id, name, loan_amount, application_date, status FROM ug_loan_applications WHERE user_id = ? ORDER BY application_date DESC";
         List<LoanStatusDTO> list = fetchLoanStatus(query, userId);
         list.forEach(dto -> dto.setLoanType("UG"));  // Set loan type here
         return list;
     }


    public List<LoanStatusDTO> fetchPGLoanStatus(String userId) {
        System.out.println("PG FETCHED");
        String query = "SELECT application_id, name, loan_amount, application_date, status FROM pg_loan_applications WHERE user_id = ? ORDER BY application_date DESC";
        List<LoanStatusDTO> list = fetchLoanStatus(query, userId);
        list.forEach(dto -> dto.setLoanType("PG"));  // Set loan type here
        return list;
    }


    public List<LoanStatusDTO> fetchAbroadLoanStatus(String userId)
    {
        System.out.println("ABROAD FETCHED");
        String query = "SELECT application_id, name, loan_amount, application_date, status FROM abroad_loan_applications WHERE user_id = ? ORDER BY application_date DESC";
        List<LoanStatusDTO> list = fetchLoanStatus(query, userId);
        list.forEach(dto -> dto.setLoanType("ABROAD"));  // Set loan type here
        return list;
    }


    private List<LoanStatusDTO> fetchLoanStatus(String sql, String userId) {
        List<LoanStatusDTO> list = new ArrayList<>();

        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next()) {
                    LoanStatusDTO dto = new LoanStatusDTO();
                    dto.setApplicationId(rs.getString("application_id"));
                    dto.setName(rs.getString("name"));
                    dto.setLoanAmount(rs.getString("loan_amount"));
                    dto.setApplicationDate(rs.getTimestamp("application_date").toString());
                    dto.setStatus(rs.getString("status"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean isPendingUgLoanRepo(String userID)
    {
        String query = "SELECT * FROM ug_loan_applications WHERE user_id = ? and status = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userID);
            preparedStatement.setString(2,"Loan Sanction");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean isPendingPgLoanRepo(String userID)
    {
        String query = "SELECT * FROM pg_loan_applications WHERE user_id = ? and status = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userID);
            preparedStatement.setString(2,"Loan Sanction");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean isPendingAbroadLoanRepo(String userID)
    {
        String query = "SELECT * FROM abroad_loan_applications WHERE user_id = ? and status = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userID);
            preparedStatement.setString(2,"Loan Sanction");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public void deleteAllApplicationsByUserId(String userId) throws SQLException
    {
        String[] deleteQueries =
                {
                "DELETE FROM ug_loan_applications WHERE user_id = ?",
                "DELETE FROM pg_loan_applications WHERE user_id = ?",
                "DELETE FROM abroad_loan_applications WHERE user_id = ?"
        };

        try (Connection conn = databaseDAO.getConnection())
        {
            for (String query : deleteQueries) {
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, userId);
                    ps.executeUpdate();
                }
            }
        }
    }

    public boolean hasActiveOrPendingLoan(String userId) throws SQLException {
        String sql = """
        SELECT 1 FROM ug_loan_applications
        WHERE user_id = ? AND status IN ('Processing', 'Repayment Started','Loan Sanction')
        UNION
        SELECT 1 FROM pg_loan_applications 
        WHERE user_id = ? AND status IN ('Processing', 'Repayment Started','Loan Sanction')
        UNION
        SELECT 1 FROM abroad_loan_applications 
        WHERE user_id = ? AND status IN ('Processing', 'Repayment Started','Loan Sanction')
        LIMIT 1
    """;

        try (Connection conn = databaseDAO.getConnection();  // Your DB connector
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, userId);
            stmt.setString(3, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // returns true if at least one record exists
            }
        }
    }





    public boolean deletingTransactionHistoryRepo(String userId)
    {
        String query = "DELETE FROM transaction_history WHERE Sender_id = ? OR Receiver_id = ?";

        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,userId);
            preparedStatement.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean deletingNotificationRepo(String userId)
    {
        String query = "DELETE FROM notifications WHERE userId = ?";

        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userId);
            preparedStatement.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deletingAccountRepo(String userId)
    {
        String query = "DELETE FROM users WHERE aadhar = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userId);

            preparedStatement.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public LoanDetails findLoanByApplicationId(String applicationId)
    {
        String sql = "SELECT application_id, name, aadhaar_card, loan_amount FROM ug_loan_applications WHERE application_id = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LoanDetails(
                        rs.getString("application_id"),
                        rs.getString("name"),
                        rs.getString("aadhaar_card"),
                        rs.getString("loan_amount")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public LoanDetails findPgLoanByApplicationId(String applicationId)
    {
        String sql = "SELECT application_id, name, aadhaar_card, loan_amount FROM pg_loan_applications WHERE application_id = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LoanDetails(
                        rs.getString("application_id"),
                        rs.getString("name"),
                        rs.getString("aadhaar_card"),
                        rs.getString("loan_amount")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoanDetails findAbroadLoanByApplicationId(String applicationId)
    {
        String sql = "SELECT application_id, name, aadhaar_card, loan_amount FROM abroad_loan_applications WHERE application_id = ?";
        try (Connection conn = databaseDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LoanDetails(
                        rs.getString("application_id"),
                        rs.getString("name"),
                        rs.getString("aadhaar_card"),
                        rs.getString("loan_amount")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String gettingUserIDbyAppIDrepo(String app_id)
    {
        String query = "";
        if(app_id.contains("UG"))
        {
            query = "SELECT user_id FROM ug_loan_applications WHERE application_id = ?";
        }
        if(app_id.contains("PG"))
        {
            query = "SELECT user_id FROM pg_loan_applications WHERE application_id = ?";
        }
        if(app_id.contains("ABROAD"))
        {
            query = "SELECT user_id FROM abroad_loan_applications WHERE application_id = ?";
        }
        String user_ID = "";
        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,app_id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                user_ID = rs.getString("user_id");
            }
        }
        catch (Exception e)
        {
            System.out.println("Loan repo 461"+e.getMessage());
        }
        return user_ID;
    }
    public String gettingSanchenedAmount(String app_id)
    {
        String query= "";
        if(app_id.contains("UG"))
        {
            query = "SELECT loan_amount FROM ug_loan_applications WHERE application_id = ?";
        }
        if(app_id.contains("PG"))
        {
            query = "SELECT loan_amount FROM pg_loan_applications WHERE application_id = ?";
        }
        if(app_id.contains("ABROAD"))
        {
            query = "SELECT loan_amount FROM abroad_loan_applications WHERE application_id = ?";
        }
        try (Connection connection = databaseDAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, app_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("loan_amount");
            } else {
                return null; // no record found
            }
        } catch (Exception e) {
            System.out.println("loan repo -492"+e.getMessage());
            return null;
        }
    }


    public List<String> gettingPendingLoanRepo()
    {
        List<String> pendingLoan = new ArrayList<>();
        String[] queries =
                {
                "SELECT user_id FROM ug_loan_applications WHERE status = 'Processing'",
                "SELECT user_id FROM pg_loan_applications WHERE status = 'Processing'",
                "SELECT user_id FROM abroad_loan_applications WHERE status = 'Processing'"
        };

        try (Connection connection = databaseDAO.getConnection())
        {
            int num = 0;
            for (String query : queries)
            {
                num++;
                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet rs = preparedStatement.executeQuery())
                {
                    while (rs.next())
                    {
                        if(num==1)
                        {
                            pendingLoan.add("UG"+rs.getString("user_id"));
                        }
                        if(num==2)
                        {
                            pendingLoan.add("PG"+rs.getString("user_id"));
                        }
                        if(num==3)
                        {
                            pendingLoan.add("ABROAD"+rs.getString("user_id"));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while fetching pending loans: " + e.getMessage());
        }
        return pendingLoan;
    }

    public LoanUg getingAllDetailsofUG(String userId)
    {
        LoanUg obj = new LoanUg();
        String query = "SELECT * FROM ug_loan_applications WHERE user_id = ? AND status = 'Processing'";
        try (Connection connection = databaseDAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                obj.setApplicationId(rs.getString("application_id"));
                obj.setUserId(rs.getString("user_id"));
                obj.setName(rs.getString("name"));
                obj.setAge(rs.getString("age"));
                obj.setDob(String.valueOf(rs.getDate("dob")));
                obj.setTenthPercentage(rs.getString("tenth_percentage"));
                obj.setTwelfthPercentage(rs.getString("twelfth_percentage"));
                obj.setCourseDuration(rs.getString("course_duration"));
                obj.setCounselingCode(rs.getString("counseling_code"));
                obj.setAadhaarCard(rs.getString("aadhaar_card"));
                obj.setPanCard(rs.getString("pan_card"));
                obj.setFatherIncome(rs.getString("father_income"));
                obj.setLoanAmount(rs.getString("loan_amount"));
                obj.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public LoanPg getingAllDetailsofPG(String userId)
    {
        LoanPg obj = new LoanPg();
        String query = "SELECT * FROM pg_loan_applications WHERE user_id = ? AND status = 'Processing'";
        try (Connection connection = databaseDAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                obj.setApplicationId(rs.getString("application_id"));
                obj.setUserId(rs.getString("user_id"));
                obj.setName(rs.getString("name"));
                obj.setAge(rs.getString("age"));
                obj.setDob(String.valueOf(rs.getDate("dob")));
                obj.setTenthPercentage(rs.getString("tenth_percentage"));
                obj.setTwelfthPercentage(rs.getString("twelfth_percentage"));
                obj.setFinalCgpa(rs.getString("final_cgpa"));
                obj.setCourseDuration(rs.getString("course_duration"));
                obj.setCounselingCode(rs.getString("counseling_code"));
                obj.setAadhaarCard(rs.getString("aadhaar_card"));
                obj.setPanCard(rs.getString("pan_card"));
                obj.setFatherIncome(rs.getString("father_income"));
                obj.setLoanAmount(rs.getString("loan_amount"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public LoanAbroad getingAllDetailsofABROAD(String userId)
    {
        LoanAbroad obj = new LoanAbroad();
        String query = "SELECT * FROM abroad_loan_applications WHERE user_id = ?";
        try (Connection connection = databaseDAO.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                obj.setApplicationId(rs.getString("application_id"));
                obj.setUserId(rs.getString("user_id"));
                obj.setName(rs.getString("name"));
                obj.setAge(rs.getString("age"));
                obj.setDob(String.valueOf(rs.getDate("dob")));
                obj.setTenthPercentage(rs.getString("tenth_percentage"));
                obj.setTwelfthPercentage(rs.getString("twelfth_percentage"));
                obj.setApplyingFor(rs.getString("applying_for"));
                obj.setCourseDuration(rs.getString("course_duration"));
                obj.setAadhaarCard(rs.getString("aadhaar_card"));
                obj.setPanCard(rs.getString("pan_card"));
                obj.setFatherIncome(rs.getString("father_income"));
                obj.setTravelExpenses(rs.getString("travel_expenses"));
                obj.setLoanAmount(rs.getString("loan_amount"));
            }
        } catch (SQLException e)
        {
            System.out.println("Loan repo - 633");
        }
        return obj;
    }

    public String GettingapplicationId(String type,String userId)
    {
        String query  ="";
        if(type.equals("UG")) query = "SELECT application_id FROM ug_loan_applications WHERE user_id = ? and status = 'Processing'";
        if(type.equals("PG")) query = "SELECT application_id FROM pg_loan_applications WHERE user_id = ? and status = 'Processing'";
        if(type.equals("ABROAD"))  query = "SELECT application_id FROM abroad_loan_applications WHERE user_id = ? and status = 'Processing'";
        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,userId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return rs.getString("application_id");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "Not found";
    }

    public void UpdateRejectedLoanApplication(String app_id)
    {
        System.out.println("Application id : "+app_id);
        String query = "";
        if(app_id.contains("UG")) query = "UPDATE ug_loan_applications SET status ='Rejected' WHERE application_id = ?";
        if(app_id.contains("PG")) query = "UPDATE pg_loan_applications SET status ='Rejected' WHERE application_id = ?";
        if(app_id.contains("ABROAD")) query = "UPDATE abroad_loan_applications SET status ='Rejected' WHERE application_id = ?";

        try(Connection connection = databaseDAO.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,app_id);
            int affected = preparedStatement.executeUpdate();
            if(affected>0)
            {
                System.out.println("done");
            }
            else
            {
                System.out.println("not done");
            }
        }
        catch (Exception E)
        {
            System.out.println("Loan repo - 683"+E.getMessage());
        }
    }


    public void UpdateSantionedLoanApplication(String app_id)
    {
        System.out.println("Application id : "+app_id);
        String query = "";
        if(app_id.contains("UG")) query = "UPDATE ug_loan_applications SET status ='Loan Sanction' WHERE application_id = ?";
        if(app_id.contains("PG")) query = "UPDATE pg_loan_applications SET status ='Loan Sanction' WHERE application_id = ?";
        if(app_id.contains("ABROAD")) query = "UPDATE abroad_loan_applications SET status ='Loan Sanction' WHERE application_id = ?";

        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1,app_id);
            int affected = preparedStatement.executeUpdate();
            if(affected>0)
            {
                System.out.println("done");
            }
            else
            {
                System.out.println("not done");
            }
        }
        catch (Exception E)
        {
            System.out.println("loan repo -712"+E.getMessage());
        }
    }

}
