package core.english.mse2023.dto.inlineButton;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class InlineBtnDTOEncoder {
    public String delimiter = " ";

    /**
     * Groups data from the object to string
     * @return String containing object's data
     *
     * @throws ButtonCallbackDataLimitExceedException If packed data exceeds size limit
     */
    public String encode(InlineBtnDTO btnDTO) throws ButtonCallbackDataLimitExceedException {
        String string = btnDTO.command + delimiter + btnDTO.stateIndex + delimiter + btnDTO.data;

        // Checking if button's data is not exceeding the storage limit
        if (string.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new ButtonCallbackDataLimitExceedException("Data package is too big. " +
                    "Consider using smaller command name or/and data strings. " +
                    "Current package size: " + string.getBytes(StandardCharsets.UTF_8).length);
        }

        return string;
    }

    /**
     * Creates object based on packed string
     * @param data String containing data for this class
     * @return New object with data from the string
     * @throws InlineButtonCallbackDataParseException If input string isn't originated from this class
     */
    public InlineBtnDTO decode(String data) throws InlineButtonCallbackDataParseException {

        // Checking if data is not exceeding the storage limit
        if (data.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new InlineButtonCallbackDataParseException("Data string is bigger then expected! This isn't InlineButton data.");
        }

        String[] dataSplit = data.split(delimiter);

        InlineBtnDTO instance = new InlineBtnDTO();

        // Checking if data string contain the expected number of parameters
        if (dataSplit.length != InlineBtnDTO.class.getDeclaredFields().length) {
            throw new InlineButtonCallbackDataParseException("Cannot parse data from input string. Wrong parameters number.");
        } else {
            instance.command = dataSplit[0];
            instance.stateIndex = Integer.parseInt(dataSplit[1]);
            instance.data = dataSplit[2];
        }

        return instance;
    }
}
