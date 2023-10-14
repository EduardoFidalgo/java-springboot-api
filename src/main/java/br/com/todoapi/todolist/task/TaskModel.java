package br.com.todoapi.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String title;   
    
    private UUID userId;
    private String priority;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("Max 50 characteres");
        }

        this.title = title;
    }

    public void setDescription(String description) throws Exception {
    if (title.length() > 50) {
        throw new Exception("Max 255 characteres");
    }

    this.description = description;
    }
}