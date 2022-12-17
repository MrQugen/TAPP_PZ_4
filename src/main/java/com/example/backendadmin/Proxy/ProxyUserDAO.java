package com.example.backendadmin.Proxy;

import com.example.backendadmin.DAO.CloudscapeDAOFactory;
import com.example.backendadmin.DAO.UserDAO;
import com.example.backendadmin.model.User;

import java.util.List;

public class ProxyUserDAO implements UserDAO {

    private final CloudscapeDAOFactory service;
    String role;

    public ProxyUserDAO(User user){
        this.service = new CloudscapeDAOFactory();
        this.role = user.getRole();
    }
    public ProxyUserDAO(String role){
        this.service = new CloudscapeDAOFactory();
        this.role = role;
    }
    public ProxyUserDAO(){
        this.service = new CloudscapeDAOFactory();
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public List<User> index() {
        if(role.equals("[ROLE_ADMIN]")) {
            return service.getUserDAO().index();
        }
        else {
            System.out.println("Error index! You don't have enough rights.");
        }
        return null;
    }

    @Override
    public User show(String login) {
        if(role.equals("[ROLE_ADMIN]") || role.equals("[ROLE_USER]")) {
            return service.getUserDAO().show(login);
        }
        else {
            System.out.println("Error show! You don't have enough rights.");
        }
        return null;
    }

    @Override
    public void save(User NewUser) {
        if(role.equals("[ROLE_ANONYMOUS]")) {
            service.getUserDAO().save(NewUser);
        }
        else {
            System.out.println("Error save! You don't have enough rights.");
        }
    }

    @Override
    public void update(String login, User updateUser) {
        if(role.equals("[ROLE_ADMIN]") || role.equals("[ROLE_USER]")) {
            service.getUserDAO().update(login,updateUser);
        }
        else {
            System.out.println("Error update! You don't have enough rights.");
        }
    }

    @Override
    public void update_user_info(String login, User updateUser) {
        if(role.equals("[ROLE_ADMIN]") || role.equals("[ROLE_USER]")) {
            service.getUserDAO().update_user_info(login,updateUser);
        }
        else {
            System.out.println("Error update_user_info! You don't have enough rights.");
        }
    }

    @Override
    public void update_user_security(String login, User updateUser) {
        if(role.equals("[ROLE_ADMIN]") || role.equals("[ROLE_USER]")) {
            service.getUserDAO().update_user_security(login,updateUser);
        }
        else {
            System.out.println("Error update_user_security! You don't have enough rights.");
        }
    }

    @Override
    public void delete(int id) {
        if(role.equals("[ROLE_ADMIN]")) {
            service.getUserDAO().delete(id);
        }
        else {
            System.out.println("Error delete! You don't have enough rights.");
        }
    }
}
