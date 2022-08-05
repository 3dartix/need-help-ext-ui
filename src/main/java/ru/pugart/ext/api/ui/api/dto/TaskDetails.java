package ru.pugart.ext.api.ui.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDetails {
    private String title;
    private String shortDescription;
    private String fullDescription;
    private BigDecimal startAmount;
    private Instant actualFrom;
    private Instant actualTo;
    private BigDecimal finalAmount;
    private List<String> images;
    private Float lat;
    private Float lon;
    private Boolean performed;
    private Integer ratingPerformer;
    private String performer;
    private String commentAuthor;
    private String commentPerformer;
    private Integer ratingAuthor;
    private BigDecimal receivedAmount;
}
