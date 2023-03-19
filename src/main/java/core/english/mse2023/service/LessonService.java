package core.english.mse2023.service;

import core.english.mse2023.model.Lesson;

import java.util.UUID;

public interface LessonService {

    Lesson getLessonById(UUID id);

}
