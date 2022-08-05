package ru.pugart.ext.api.ui.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String phone;
    private String email;
    private String telegram;
//    private List<Task> tasksAuthor;
//    private List<Task> tasksPerformer;
//    private Boolean verified;
//    private Integer rating;
    private Boolean isBlocked;
    private List<String> roles;
}
