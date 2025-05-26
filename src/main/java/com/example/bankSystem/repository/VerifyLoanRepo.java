package com.example.bankSystem.repository;

import com.example.bankSystem.DAO.DatabaseDAO;
import org.bytedeco.tesseract.PAGE_RES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class VerifyLoanRepo
{
    @Autowired
    private DatabaseDAO databaseDAO;

    public boolean checkNameUg(String aadhar,String name)
    {
        String query = "SELECT fullname FROM users WHERE aadhar = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setString(1,aadhar);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                System.out.println(rs.getString("fullname") +" trimmerd - "+name);
                if(rs.getString("fullname").equalsIgnoreCase(name.trim()))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean checkPanUg(String userid,String pan)
    {
        String query = "SELECT pan_id FROM users WHERE aadhar = ?";
        try(Connection connection = databaseDAO.getConnection();
            PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setString(1,userid);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                if(rs.getString("pan_id").equalsIgnoreCase(pan.trim()))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
