package core.english.mse2023.service.entity.user;

import core.english.mse2023.service.entity.Subscription;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "student")
public class Student extends User {

    @ManyToMany(mappedBy = "children")
    private Set<Parent> parents;

    @JoinColumn(name = "subscription")
    @OneToMany
    private Set<Subscription> subscriptions;
}
