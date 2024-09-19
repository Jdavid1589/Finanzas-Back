package appsys.free.constru_app.auth.service;

import appsys.free.constru_app.auth.entity.UserApp;


import java.util.List;

public interface IUserService {
    UserApp findByUserName(String userName);

    boolean saveUser(UserApp user);









    boolean validUser(String user);


}
