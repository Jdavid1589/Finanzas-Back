package appsys.free.Agrocore.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Rol implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, length = 30)
    private String name;
}
