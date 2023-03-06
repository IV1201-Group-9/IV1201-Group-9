package com.iv1201.recapp.Service;

import com.iv1201.recapp.Config.Exceptions.ApplicationCouldNotSubmitException;
import com.iv1201.recapp.Config.Exceptions.CouldNotFindCompetencesException;
import com.iv1201.recapp.Integration.AvailabilityRepo;
import com.iv1201.recapp.Integration.CompetenceProfileRepo;
import com.iv1201.recapp.Integration.CompetenceRepo;
import com.iv1201.recapp.Integration.UserRepo;
import com.iv1201.recapp.Models.ApplicantDTOs.ApplicationDTO;

import com.iv1201.recapp.Models.ApplicantDTOs.DatesDTO;
import com.iv1201.recapp.Models.Availability;
import com.iv1201.recapp.Models.Competence;
import com.iv1201.recapp.Models.CompetenceProfile;
import com.iv1201.recapp.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class ApplicationService {

    @Autowired
    private CompetenceRepo competenceRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AvailabilityRepo availabilityRepo;

    @Autowired
    private CompetenceProfileRepo competenceProfileRepo;

    public List<Competence> getAllCompetence() throws CouldNotFindCompetencesException {
        List<Competence> competences = null;

        try {
            competences = competenceRepo.findAllCompetences();
            if(competences.size() == 0){
                throw new IllegalArgumentException();
            }
        }catch (Exception e ){
            throw new CouldNotFindCompetencesException("Failed to fetch competences");
        }
        return competences;
    }

  public void submitApplication(ApplicationDTO applicationDTO) throws ApplicationCouldNotSubmitException {
        try {
            String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
            User authUser = userRepo.findUserByUsername(principalName);
            authUser.setFirstname(applicationDTO.getFirstName());
            authUser.setSurname(applicationDTO.getLastName());
            authUser.setPnr(applicationDTO.getPersonalNumber());
            //authUser.setStatus("Unhandled");
            userRepo.save(authUser);

            applicationDTO.getDatesDTOList().forEach(date ->
            {
                try {
                    availabilityRepo.save(
                            new Availability(
                                    authUser,
                                    convertStringDate(date.getFrom_date()),
                                    convertStringDate(date.getTo_date())));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });

            applicationDTO.getAreaOfExpertiseDTOList().forEach(expertise ->
                competenceProfileRepo.save(new CompetenceProfile(
                       authUser,
                        competenceRepo.findCompetenceById(expertise.getAreaOfExpertiseID()),
                        expertise.getYearsOfExperience())));

             */
        }catch (Exception e){
            throw new ApplicationCouldNotSubmitException("The application could not be submitted");
        }
    }

    private Date convertStringDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = formatter.parse(str);
        return date;
    }

//    todo deleteApplication if there is time
//    public void deleteApplication(){
//        try{
//            String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
//            User authUser = userRepo.findUserByUsername(principalName);
//            authUser.setApplicationStatus(null);
//            availabilityRepo.deleteAll
//
//        }catch (Exception e){
//
//        }
//        System.out.println("Deleted Application");
//
//    }
}



