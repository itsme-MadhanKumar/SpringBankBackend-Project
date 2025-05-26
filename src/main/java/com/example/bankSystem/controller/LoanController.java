package com.example.bankSystem.controller;
import com.example.bankSystem.model.AbroadLoanRequest;
import com.example.bankSystem.model.LoanStatusDTO;
import com.example.bankSystem.model.PGLoanRequest;
import com.example.bankSystem.model.UGLoanApplicationRequest;
import com.example.bankSystem.repository.UserRepo;
import com.example.bankSystem.service.MailSender;
import com.example.bankSystem.service.OtpGen;
import com.example.bankSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
public class LoanController
{
    @Autowired
    private UserService service;
    @Autowired
    UserRepo repo;
    @Autowired
    private OtpGen otpGen;
    @Autowired
    private MailSender mailSender;

    @PostMapping("/api/loan/ug")
    public ResponseEntity<?> submitUGLoan(@RequestPart("data") UGLoanApplicationRequest dto, @RequestPart("collateralDocuments") MultipartFile[] collateralDocuments) throws SQLException
    {
        if(dto.getUserId().equals(dto.getAadhaarCard()))
        {
            service.processUGLoan(dto,collateralDocuments);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Invalid Aadhar Number"));
    }

    @PostMapping("/api/loan/pg")
    public ResponseEntity<?> submitPgLoan(@RequestPart("data") PGLoanRequest pgLoanDTO, @RequestPart("collateralDocuments") List<MultipartFile> collateralDocuments) throws Exception
    {
        Map<String, String> response = new HashMap<>();

            if(pgLoanDTO.getAadhaarCard().equals(pgLoanDTO.getUserId()))
            {
                boolean flag = service.processPgLoan(pgLoanDTO);
                if(flag)
                {
                    return ResponseEntity.ok(Collections.singletonMap("success", true));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Invalid Aadhar Number , Enter your Aadhar correctly"));
    }

    @PostMapping("/api/loan/abroad")
    public ResponseEntity<Map<String, Object>> submitAbroadLoan(@RequestPart("data") AbroadLoanRequest abroadLoanDTO,
            @RequestPart("collateralDocuments") List<MultipartFile> collateralDocuments) throws SQLException {

        Map<String, Object> response = new HashMap<>();

        if (abroadLoanDTO.getUserId().equals(abroadLoanDTO.getAadhaarCard())) {
            boolean flag = service.saveLoanApplicationAbroad(abroadLoanDTO);
            if (flag) {
                response.put("success", true);
                response.put("message", "Abroad loan application submitted successfully.");
                return ResponseEntity.ok(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Invalid Aadhar Number, Enter your Aadhar correctly.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", false);
        response.put("message", "Failed to submit abroad loan application.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @GetMapping("api/loan/check-applied/{type}/{userId}")
    public ResponseEntity<Map<String, Boolean>> checkLoanApplication(
            @PathVariable String type,
            @PathVariable String userId) throws SQLException {

        boolean alreadyApplied = service.hasUserAppliedForAnyLoan(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("alreadyApplied", alreadyApplied);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/loan-status")
    public List<LoanStatusDTO> getLoanStatus(@RequestParam String userId)
    {
        System.out.println("Frinerned.........................."+userId);
        return service.getLoanStatus(userId);
    }
}
