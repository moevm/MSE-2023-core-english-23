package core.english.mse2023.dto;

import lombok.*;

/**
 * This class represents data package for content of Data parameter of an inlineButton <br>
 * Be mindful that the size of all the data with spacing symbol must not exceed 64 bytes!
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InlineButtonDTO {

    String command;

    int stateIndex;

    String data;
}
