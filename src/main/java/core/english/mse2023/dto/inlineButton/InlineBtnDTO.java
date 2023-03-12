package core.english.mse2023.dto.inlineButton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represents data package for content of Data parameter of an inlineButton
 * Be mindful that the size of all the data with spacing symbol must not exceed 64 bytes!
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InlineBtnDTO {

    String command;
    int stateIndex;
    String data;
}
