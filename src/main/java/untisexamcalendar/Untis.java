/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package untisexamcalendar;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import untisexamcalendar.googlecalendar.GoogleCalendarAPI;
import untisexamcalendar.untisapi.UntisAPI;

/**
 *
 * @author EweLo
 */
public class Untis {

    private String user;
    private String key;
    
    private UntisAPI untisAPI = new UntisAPI();
    
    private Exams exams = new Exams();

    public Untis(String user, String key) throws Exception {
        this.user = user;
        this.key = key;
        
        untisAPI.login(user, key);
        
        JSONObject examResponse = untisAPI.requestExams("20200915", "20210715", true, -1);
        JSONArray examArray = examResponse.getJSONObject("data").getJSONArray("exams");
        
        for (int i = 0; i < examArray.length(); i++) {
            JSONObject j = examArray.getJSONObject(i);
            Exam exam = new Exam();
            
            exam.setName(j.getString("name"));
            exam.setSubject(j.getString("subject"));
            exam.setText(j.getString("text"));
            exam.setExamType(j.getString("examType"));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            
            String startTime = j.getInt("startTime") + "";
            String endTime = j.getInt("endTime") + "";
            
            if (startTime.length() < 4) {
                startTime = "0" + startTime;
            }
            
            if (endTime.length() < 4) {
                endTime = "0" + endTime;
            }
            
            
            exam.setStartTime(LocalDateTime.parse(j.getInt("examDate") + "" + startTime, formatter));
            exam.setEndTime(LocalDateTime.parse(j.getInt("examDate") + "" + endTime, formatter));
            
            exams.add(exam);
            
            System.out.println(exam.toString());
        }
    }

    public String getUser() {
        return user;
    }

    public Exams getExams() {
        return exams;
    }
    
}
