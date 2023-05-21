package core.english.mse2023.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ThymeleafLessonDTO {
    private String topic;
    private String date;
    private String status;
}
