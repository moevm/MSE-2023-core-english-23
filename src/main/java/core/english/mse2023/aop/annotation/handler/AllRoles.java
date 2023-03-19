package core.english.mse2023.aop.annotation.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AdminRole
@ParentRole
@StudentRole
@TeacherRole
@Retention(RetentionPolicy.RUNTIME)
public @interface AllRoles {
}
