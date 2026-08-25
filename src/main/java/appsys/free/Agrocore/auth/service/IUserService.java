package appsys.free.Agrocore.auth.service;

import appsys.free.Agrocore.auth.entity.UserApp;

public interface IUserService {
    UserApp findByUserName(String userName);

    boolean saveUser(UserApp user);


    boolean validUser(String user);


}
