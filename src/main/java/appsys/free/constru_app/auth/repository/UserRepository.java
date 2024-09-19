package appsys.free.constru_app.auth.repository;


import appsys.free.constru_app.auth.entity.Rol;
import appsys.free.constru_app.auth.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserApp,Integer> {
   UserApp findByUserName(String userName);

    UserApp findByNoDocument(String noDocument);
//
//
//    @Query("SELECT u FROM Usuario u INNER JOIN u.roles r WHERE r.id=2")
//    List<UserApp> getListOperarios(Rol rol);
//
//

    @Query("SELECT u FROM UserApp u WHERE u.userName=?1 AND u.enable=true")
    UserApp validUser(String user);

  //public int addUsser(@Param("id")Integer id, @Param("userName")String userName, @Param("passw")String passw, @Param("nombres")String nombres, @Param("apellidos")String apellidos, @Param("email")String email, @Param("telefonos")String telefonos, @Param("noDocumento")String noDocumento, @Param("enable")Boolean enable, @Param("sinc")Boolean sinc, @Param("update")Boolean update);
}
