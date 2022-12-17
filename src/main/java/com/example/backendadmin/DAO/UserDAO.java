package com.example.backendadmin.DAO;

import com.example.backendadmin.model.User;

public interface UserDAO {
    public Object index();
    public User show(String login);
    public void save(User NewUser);
    public void update(String login, User updateUser);
    public void update_user_info(String login, User updateUser);
    public void update_user_security(String login, User updateUser);
    public void delete(int id);

}
