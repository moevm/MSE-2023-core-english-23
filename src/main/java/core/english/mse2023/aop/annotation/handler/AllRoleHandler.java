package core.english.mse2023.aop.annotation.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AdminHandler
@ParentHandler
@StudentHandler
@TeacherHandler
@Retention(RetentionPolicy.RUNTIME)
public @interface AllRoleHandler {
}
