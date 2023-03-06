package com.iv1201.recapp.Controller;

import com.iv1201.recapp.Config.Exceptions.StatusDTOException;
import com.iv1201.recapp.Models.RecruiterDTOs.SingleUserApplicationDTO;
import com.iv1201.recapp.Models.RecruiterDTOs.StatusDTO;
import com.iv1201.recapp.Models.RecruiterDTOs.UpdatedStatusDTO;
import com.iv1201.recapp.Service.RecruiterService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiters")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/applicants")
    public ResponseEntity<List<SingleUserApplicationDTO>> getAllApplicants()
            throws StatusDTOException {
        return ResponseEntity.ok(recruiterService.getAllApplicants());
    }

    @PostMapping("/status")
    public ResponseEntity<UpdatedStatusDTO> setStatus(@RequestBody @Valid StatusDTO statusDTO)
            throws StatusDTOException {
        recruiterService.updateStatus(statusDTO);
        return ResponseEntity.ok(new UpdatedStatusDTO("Status updated successfully"));

    }
}


