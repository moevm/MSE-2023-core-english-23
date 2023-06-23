package core.english.mse2023.model.dictionary;

public enum UserRole {
    STUDENT("Ученик"),
    PARENT("Родитель"),
    TEACHER("Учитель"),
    ADMIN("Админ");

    private final String string;

    UserRole(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
