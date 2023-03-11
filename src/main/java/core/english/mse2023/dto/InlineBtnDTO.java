package core.english.mse2023.dto;

import core.english.mse2023.constant.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * This class represents data package for content of Data parameter of an inlineButton
 * Be mindful that the size of all the data with spacing symbol must not exceed 64 bytes!
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InlineBtnDTO {

    public static String delimiter = " ";
    public static int parametersNumber = 3;

    String command;
    int stateIndex;
    String data;

    @Override
    public String toString() {

        String string = command + delimiter + stateIndex + delimiter + data;

        // Checking if button's data is not exceeding the storage limit
        if (string.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new RuntimeException("Data package is too big. " +
                    "Consider using smaller command name or/and data strings. " +
                    "Current package size: " + string.getBytes(StandardCharsets.UTF_8).length);
        }

        return string;
    }

    public static InlineBtnDTO createFromString(String data) {

        // Checking if data is not exceeding the storage limit
        if (data.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new IllegalArgumentException("Data string is bigger then expected! This isn't InlineButton data.");
        }

        String[] dataSplit = data.split(delimiter);

        InlineBtnDTO instance = new InlineBtnDTO();

        // Checking if data string contain the expected number of parameters
        if (dataSplit.length != parametersNumber) {
            throw new IllegalArgumentException("Cannot parse data from input string. Wrong parameters number.");
        } else {
            instance.command = dataSplit[0];
            instance.stateIndex = Integer.parseInt(dataSplit[1]);
            instance.data = dataSplit[2];
        }

        return instance;
    }
}
