package appsys.free.constru_app.auth.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
public class UserApp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,length = 50)
    private String userName;
    private String passw;
    private String names;
    private String surnames;
    private String email;
    private String phoneNumber;
    private String noDocument;
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="users_roles", joinColumns= @JoinColumn(name="users_id"),
            inverseJoinColumns=@JoinColumn(name="roles_id"),
            uniqueConstraints= {@UniqueConstraint(columnNames= {"users_id", "roles_id"})})

    private List<Rol> roles;

}
