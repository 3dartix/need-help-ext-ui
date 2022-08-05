package ru.pugart.ext.api.ui.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String id;
    private String author;
    private TaskDetails taskDetails;
    private Category taskCategory;
    private List<PerformerOrder> performerOrders;
}
