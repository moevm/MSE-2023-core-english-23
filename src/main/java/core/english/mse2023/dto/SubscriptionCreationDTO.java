package core.english.mse2023.dto;


import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.state.subcriptionCreate.InitializedState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * POJO for storing temporary subscription data
 */
@ToString
@Getter
@Setter
public class SubscriptionCreationDTO {

    private String teacherTelegramId;

    private SubscriptionType type;

    private String studentTelegramId;

    private int lessonsRest;

    private String startDate;

    private String endDate;

}
