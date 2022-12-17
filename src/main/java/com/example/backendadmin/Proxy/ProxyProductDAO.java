package com.example.backendadmin.Proxy;

import com.example.backendadmin.DAO.CloudscapeDAOFactory;
import com.example.backendadmin.DAO.CloudscapeProductDAO;
import com.example.backendadmin.DAO.ProductDAO;
import com.example.backendadmin.model.Product;
import com.example.backendadmin.model.User;

import java.util.List;

public class ProxyProductDAO implements ProductDAO {

    private final CloudscapeDAOFactory service;
    String role;

    public ProxyProductDAO() {
        this.service = new CloudscapeDAOFactory();
    }
    public ProxyProductDAO(User user){
        this.service = new CloudscapeDAOFactory();
        this.role = user.getRole();
    }
    public ProxyProductDAO(String role){
        this.service = new CloudscapeDAOFactory();
        this.role = role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public List<Product> index() {
        return service.getProductDAO().index();
    }

    @Override
    public Product show(int id) {
        if(role.equals("[ROLE_ADMIN]")) {
            return service.getProductDAO().show(id);
        }
        else {
            System.out.println("Error show! You don't have enough rights.");
        }
        return null;
    }

    @Override
    public void save(Product product) {

        if(role.equals("[ROLE_ADMIN]")) {
            service.getProductDAO().save(product);
        }
        else {
            System.out.println("Error save! You don't have enough rights.");
        }
    }

    @Override
    public void update(int id, Product updateProduct) {

        if(role.equals("[ROLE_ADMIN]")) {
            service.getProductDAO().update(id, updateProduct);
        }
        else {
            System.out.println("Error update! You don't have enough rights.");
        }

    }

    @Override
    public void delete(int id) {

        if(role.equals("[ROLE_ADMIN]")) {
            service.getProductDAO().delete(id);
        }
        else {
            System.out.println("Error delete! You don't have enough rights.");
        }

    }
}