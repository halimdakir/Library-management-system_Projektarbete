package se.iths.library.bean;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.iths.library.controller.LoginController;
import se.iths.library.controller.UserController;
import se.iths.library.entity.Login;
import se.iths.library.entity.User;
import se.iths.library.service.UserService;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Component
@ViewScoped
public class RegisterBean implements Serializable {
        private boolean visible = false;
        private Long id;
        private String email;
        private String password;
        private boolean isAdmin;
        private String fullName;
        private String birthDate;
        private String address;
        private List<User> userList = new ArrayList<>();
        private ItemBean itemBean;

        @Autowired
        UserController userController;
        @Autowired
        UserService userService;
        @Autowired
        LoginController loginController;



        public void show(){
                visible = true;
        }
        public void hide(){
                visible = false;
        }

        public void addUser(){
                var user = new User(getFullName(), getBirthDate(), getAddress());
                var login = new Login(getEmail(), getPassword(), isAdmin());
                user.setLogin(login);
                login.setUser(user);
                userService.createUser(user);

        }
        public void updateUser(Long id){
                Optional<User> member = userService.getUserById(id);
                setId(member.get().getId());
                setFullName(member.get().getFullName());
                setBirthDate(member.get().getBirthDate());
                setAddress(member.get().getAddress());
                show();
        }
        public void saveUser(String fullName, String birthDate, String address, Long id){
                User user = new User(fullName, birthDate, address);
                userController.updateUser(user, id);
                hide();
                getUsers();
        }
        public void deleteUser(Long id){
                userService.deleteUserById(id);
                getUsers();
        }
        public void getUsers(){
                Iterable<User> iterable = userService.findUsersByLogin_IsAdmin(false);//.getUserNotAdmin();//userService.getAllUsers();
                userList = StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toList());
        }
        public String adminPage() {
                getUsers();
                return "admin";
        }
        public String onFlowProcess(FlowEvent event) {
                return event.getNewStep();
        }

        //<editor-fold desc="Getter & Setter">


        public List<User> getMemberList() {
                return userList;
        }

        public void setMemberList(List<User> userList) {
                this.userList = userList;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getFullName() {
                return fullName;
        }

        public void setFullName(String fullName) {
                this.fullName = fullName;
        }

        public String getBirthDate() {
                return birthDate;
        }

        public void setBirthDate(String birthDate) {
                this.birthDate = birthDate;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public boolean isVisible() {
                return visible;
        }

        public void setVisible(boolean visible) {
                this.visible = visible;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public boolean isAdmin() {
                return isAdmin;
        }

        public void setAdmin(boolean admin) {
                isAdmin = admin;
        }

        public List<User> getUserList() {
                return userList;
        }

        public void setUserList(List<User> userList) {
                this.userList = userList;
        }
        //</editor-fold>
}