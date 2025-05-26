package com.example.bankSystem.service;
import com.example.bankSystem.repository.VerifyLoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
public class LoanService
{

    @Autowired
    VerifyLoanRepo loanRepo;
    public String checkNameServiceUG(String userId, String name)
    {
        boolean checkName = loanRepo.checkNameUg(userId,name);
        if(checkName)
        {
            return "Checked";
        }
        else
        {
            return "Mistake";
        }
    }
    public String checkAge(String age, String dob)
    {
        System.out.println("AGEEEE - "+age+"   "+dob);
        try
        {
            int inputAge = Integer.parseInt(age.trim());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dob.trim(), formatter);
            LocalDate currentDate = LocalDate.now();
            int calculatedAge = Period.between(birthDate, currentDate).getYears();
            if (inputAge == calculatedAge)
            {
                return "Checked";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error: Invalid input format";
        }
        return "Mistake";
    }

    public String checkTenth(String tenth_percentage)
    {

        if (tenth_percentage == null || tenth_percentage.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = tenth_percentage.trim().replace("%", "");
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }

        double percentage = Double.parseDouble(cleaned);

        if (percentage >= 70 && percentage<=99.9)
        {
            return "Checked";
        }
        else
        {
            return "Mistake";
        }
    }

    public String checkCGPA(String cgpa)
    {

        if (cgpa == null || cgpa.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = cgpa.trim().replace("%", "");
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }

        double percentage = Double.parseDouble(cleaned);

        if (percentage >= 8 && percentage<=10)
        {
            return "Checked";
        }
        else
        {
            return "Mistake";
        }
    }

    public String checkCourseDuriationService(String duriation)
    {
        if (duriation == null || duriation.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = duriation.trim();
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }
        double percentage = Double.parseDouble(cleaned);

        if (percentage >= 2 && percentage<=4)
        {
            return "Checked";
        }
        else
        {
            return "Mistake";
        }
    }

    public String CounselingCode(String code)
    {
        if (code == null || code.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = code.trim();
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }
        if(code.length()!=4)
        {
            return "Mistake";
        }
        return "Checked";
    }

    public String checkPanSerive(String userid,String pan)
    {
        if(loanRepo.checkPanUg(userid,pan))
        {
            return "Checked";
        }
        return "Mistake";
    }

    public String checkAnnualIncome(String income)
    {
        if (income == null || income.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = income.trim();
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }
        double percentage = Double.parseDouble(cleaned);

        if (percentage >= 500000)
        {
            return "Checked";
        }
        else
        {
            return "Mistake";
        }
    }

    public String loanNeedService(String app_id,String amount)
    {
        if (amount == null || amount.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = amount.trim();
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }
        double percentage = Double.parseDouble(cleaned);

        if(app_id.contains("UG"))
        {
            if (percentage <= 1200000)
            {
                return "Checked";
            }
            else
            {
                return "Mistake";
            }
        }
        else if(app_id.contains("PG"))
        {
            if (percentage <= 600000)
            {
                return "Checked";
            }
            else
            {
                return "Mistake";
            }
        }
      return "Mistake";
    }
    public String loanNeedServiceAbroad(String app_id,String amount,String type)
    {
        if (amount == null || amount.trim().isEmpty())
        {
            return "Mistake";
        }
        String cleaned = amount.trim();
        if (!cleaned.matches("^\\d+(\\.\\d+)?$"))
        {
            return "Mistake";
        }
        double percentage = Double.parseDouble(cleaned);
        if(app_id.contains("ABROAD"))
        {
            if (type.equals("UG"))
            {
                if(percentage<=1200000)
                {
                    return "Checked";
                }
            }
            if(type.equals("PG"))
            {
                if(percentage<=600000)
                {
                    return "Checked";
                }
            }
        }
        return "Mistake";
    }

    public String ApplyingForServie(String type)
    {
        String trim = type.trim();
        if(trim.equals("UG") || trim.equals("PG") || trim.equals("ABROAD"))
        {
            return "Checked";
        }
        return "Mistake";
    }
}
