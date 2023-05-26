package core.english.mse2023.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import core.english.mse2023.dto.GetStudentStatisticDTO;
import core.english.mse2023.dto.ThymeleafLessonDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.User;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.PDFService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

    private static final String USER_DATA_PATTERN = "%s%s";


    private final ResourceLoader resourceLoader;


    private final LessonService lessonService;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    @Override
    public InputStream generateStudentStatisticsPDF(String templateName, Subscription studentSubscription, Timestamp startDate, Timestamp endDate) throws DocumentException, IOException {
        List<Lesson> lessons = lessonService.getAllLessonsForSubscription(studentSubscription.getId(), startDate, endDate);

        String parsedTemplate = parseStudentStatisticsThymeleafTemplate(lessons, studentSubscription.getStudent(), startDate, endDate);

        return generatePdfFromHtml(parsedTemplate);
    }

    private String parseStudentStatisticsThymeleafTemplate(List<Lesson> lessons, User student, Timestamp startDate, Timestamp endDate) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        List<ThymeleafLessonDTO> lessonDTOS = new ArrayList<>();

        for (Lesson lesson : lessons) {
            ThymeleafLessonDTO lessonDTO = ThymeleafLessonDTO.builder()
                    .topic(lesson.getTopic())
                    .date(dateFormat.format(lesson.getDate()))
                    .status(lesson.getStatus().toString())
                    .build();
            lessonDTOS.add(lessonDTO);
        }


        Context context = new Context();
        context.setVariable(
                "student",
                String.format(USER_DATA_PATTERN,
                        (student.getLastName() != null) ? (student.getLastName() + " ") : "", // Student's last name if present
                        student.getName() // Student's name (always present)
                ));
        context.setVariable("startDate", dateFormat.format(startDate));
        context.setVariable("endDate", dateFormat.format(endDate));
        context.setVariable("lessons", lessonDTOS);

        return templateEngine.process("template" + File.separator + "thymeleaf_template", context);
    }


    private InputStream generatePdfFromHtml(String html) throws DocumentException, IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        loadFonts(renderer);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        renderer.finishPDF();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void loadFonts(ITextRenderer renderer) throws DocumentException, IOException {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath*:./fonts/*.ttf");

        for (Resource res: resources) {
            renderer.getFontResolver().addFont("fonts" + File.separator + res.getFilename(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }
    }

}
