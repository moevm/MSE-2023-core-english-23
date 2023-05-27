package core.english.mse2023.service;

import com.lowagie.text.DocumentException;
import core.english.mse2023.model.Subscription;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public interface PDFService {

    InputStream generateStudentStatisticsPDF(String templateName, Subscription studentSubscription, Timestamp startDate, Timestamp endDate) throws DocumentException, IOException;
}
