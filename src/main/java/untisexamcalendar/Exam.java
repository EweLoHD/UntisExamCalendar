/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package untisexamcalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author EweLo
 */
public class Exam {

    private String name;
    private String subject;
    private String text;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean selected;

    public Exam(String name, String subject, String text, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.subject = subject;
        this.text = text;
        this.startTime = startTime;
        this.endTime = endTime;
        this.selected = true;
    }
    
    public Exam() {
        this.selected = true;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Event toEvent() {
        Event event = new Event();

        event.setSummary(subject + " - " + name);
        if(!text.isEmpty()) event.setDescription(text);

        event.setStart(convertDateTime(startTime));
        event.setEnd(convertDateTime(endTime));

        return event;
    }

    private static EventDateTime convertDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.of("Europe/Berlin"));

        return new EventDateTime()
                .setDateTime(new DateTime(zdt.toInstant().toEpochMilli()))
                .setTimeZone("Europe/Berlin");
    }
    
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "<html>" + startTime.format(dateFormat) + " " + "<b>" + subject + " " + "</b>" + "<i>" + text + "</i>" + "</html>";
    }

}
