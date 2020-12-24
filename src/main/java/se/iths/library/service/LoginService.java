package se.iths.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.library.entity.Login;
import se.iths.library.repository.LoginRepository;

import java.util.Optional;


@Service
public class LoginService {
    @Autowired
    LoginRepository loginRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Login createLogin(Login login){
        login.setPassword(passwordEncoder.encode(login.getPassword()));
        return loginRepository.save(login);
    }
    public boolean checkEmailIfExist(String email){
        Optional<Login> login = loginRepository.findByEmail(email);
        if (login.isPresent()){
            return true;
        }else {
            return false;
        }
    }

}
