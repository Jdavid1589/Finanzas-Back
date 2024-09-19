package appsys.free.constru_app.auth.service;

import appsys.free.constru_app.auth.repository.UserRepository;
import appsys.free.constru_app.auth.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.util.stream.Collectors;


@Service
public class UserImplService implements UserDetailsService, IUserService {
    private Logger logger = LoggerFactory.getLogger(UserImplService.class);
    @Autowired
    private UserRepository userRepository;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserApp userApp= userRepository.findByUserName(userName);
        if (userApp == null) {

            logger.error("No existe usuario en el sistema");
        }
        List<GrantedAuthority> authorities = userApp.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
        return new User(userName, userApp.getPassw(), userApp.isEnable(), true, true, true, authorities);
    }

    @Override
    @Transactional(readOnly = true)
    public UserApp findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    @Transactional()
    @Override
    public boolean saveUser(UserApp user) {


        UserApp userResp = userRepository.findByNoDocument(user.getNoDocument());
        if (userResp == null) {
            Rol rol = new Rol();
            rol.setId(2);
            List<Rol> listRoles = new ArrayList<>();
            listRoles.add(rol);
            user.setRoles(listRoles);
            UserApp user1 = userRepository.save(user);
            if (user1 != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }

    public static String decrypt(String ciphertext) throws Exception {
        SecretKey secretKey = getSecretKey("o9szYIOq1rRMiouNhNvaq96lqUvCekxR");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));

    }

    public static SecretKey getSecretKey(String secretKey) throws Exception {
        byte[] decodeSecretKey = Base64.getDecoder().decode(secretKey);

        return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, "AES");
    }










    @Transactional(readOnly = true)
    @Override
    public boolean validUser(String user) {
        UserApp usuario = userRepository.validUser(user);
        if (usuario == null) {
            return false;
        } else {
            return true;
        }

    }


}
