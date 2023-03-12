package core.english.mse2023.dto;


import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.state.subcriptionCreate.InitializedState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * POJO for storing temporary subscription data
 */
@ToString
@Getter
@Setter
@Builder
public class SubscriptionCreationDTO {

    private String teacherTelegramId;

    private SubscriptionType type;

    private String studentTelegramId;

    private int lessonsRest;

    private Timestamp startDate;

    private Timestamp endDate;

}
