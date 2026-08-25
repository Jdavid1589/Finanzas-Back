package appsys.free.Agrocore.auth.repository;


import appsys.free.Agrocore.auth.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserApp, Integer> {
    UserApp findByUserName(String userName);

    UserApp findByNoDocument(String noDocument);


    @Query("SELECT u FROM UserApp u WHERE u.userName=?1 AND u.enable=true")
    UserApp validUser(String user);

    //public int addUsser(@Param("id")Integer id, @Param("userName")String userName, @Param("passw")String passw, @Param("nombres")String nombres, @Param("apellidos")String apellidos, @Param("email")String email, @Param("telefonos")String telefonos, @Param("noDocumento")String noDocumento, @Param("enable")Boolean enable, @Param("sinc")Boolean sinc, @Param("update")Boolean update);
}
