package com.example.backendadmin.Controllers;

import com.example.backendadmin.DAO.CloudscapeDAOFactory;
import com.example.backendadmin.DAO.UserDAO;
import com.example.backendadmin.Momento.ProductSaveHistory;
import com.example.backendadmin.Proxy.ProxyProductDAO;
import com.example.backendadmin.Proxy.ProxyUserDAO;
import com.example.backendadmin.model.Product;
import com.example.backendadmin.model.ProductBuilderIMpl;
import com.example.backendadmin.model.User;
import com.example.backendadmin.model.UserBuilderIMpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final CloudscapeDAOFactory cloudscapeDAOFactory = new CloudscapeDAOFactory();
    private final ProductSaveHistory productSaveHistory = new ProductSaveHistory();
    private ProxyUserDAO proxyUserDAO;
    private ProxyProductDAO proxyProductDAO;

    public MainController(){
        this.proxyUserDAO = new ProxyUserDAO();
        this.proxyProductDAO = new ProxyProductDAO();
    }

    @GetMapping("/")
    public String home(Model model){

        model.addAttribute("product", cloudscapeDAOFactory.getProductDAO().index());

        return "home";
    }

    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);

        return "login";
    }

    @GetMapping("/admin")
    public String admin_home(Model model) {
        return "Admin_home";
    }

    @GetMapping("/admin/edit-bait")
    public String edit_bait(Model model) {

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        model.addAttribute("product",  proxyProductDAO.index());
        return "edit-bait";
    }

    @GetMapping("/admin/edit-bait/{id}")
    public String edit_bait_details(@PathVariable(value = "id") int id, Model model) {

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        model.addAttribute("product", proxyProductDAO.show(id));

        return "edit-bait-details";
    }

    @PostMapping("/admin/edit-bait/{id}")
    public String edit_bait_update(@PathVariable(value = "id") int id, @RequestParam String name, @RequestParam String attractant,
                                   @RequestParam String type, @RequestParam double price, @RequestParam String path_to_file,
                                   @RequestParam String description, @RequestParam int amount, Model model){

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        Product product = new ProductBuilderIMpl().setProduct_id(id).setName(name).setPrice(price).setAttractant(attractant).setType(type)
                .setDescription(description).setPath_to_file(path_to_file).setAmount(amount).build();

        proxyProductDAO.update(id, product);
        productSaveHistory.setSave(product.save());

        return "redirect:/admin/edit-bait";
    }

    @GetMapping("/admin/edit-bait-history/{id}")
    public String edit_bait_history(@PathVariable(value = "id") int id, Model model) {

        model.addAttribute("product", productSaveHistory.getSave(id));

        return "edit-bait-history";
    }

    @PostMapping("/admin/edit-bait-history/{id}")
    public String edit_bait_history1(@PathVariable(value = "id") int id, @RequestParam String index, Model model){

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        Product product = new Product();
        product.load(productSaveHistory.getSaveDate(index));
        proxyProductDAO.update(id, product);
        productSaveHistory.removeDate(index);

        return "redirect:/admin/edit-bait";
    }

    @PostMapping("/admin/edit-bait")
    public String bait_delete(@RequestParam int id, Model model){

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        proxyProductDAO.delete(id);
        productSaveHistory.removeSave(id);

        return "redirect:/admin/edit-bait";
    }

    @GetMapping("/admin/add-bait")
    public String add_bait(Model model) {
        return "add-bait";
    }

    @PostMapping("/admin/add-bait")
    public String add_bait(@RequestParam String name, @RequestParam String attractant,
                           @RequestParam String type, @RequestParam double price, @RequestParam String path_to_file,
                           @RequestParam String description, @RequestParam int amount, Model model){

        proxyProductDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        proxyProductDAO.save(new ProductBuilderIMpl().setName(name).setPrice(price).setAttractant(attractant).setType(type)
                .setDescription(description).setPath_to_file(path_to_file).setAmount(amount).build());

        return "redirect:/admin/edit-bait";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @RequestMapping("/registration")
    public String registrationPost(@RequestParam String name, @RequestParam String surname, @RequestParam String patronymic,
                                   @RequestParam String telephone, @RequestParam String city, @RequestParam String email,
                                   @RequestParam String password, Model model){

        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        proxyUserDAO.save(new UserBuilderIMpl().setName(name).setSurname(surname).setPatronymic(patronymic)
                .setTelephone(telephone).setCity(city).setLogin(email).setPassword(password).build());

        return "redirect:/";
    }

    @GetMapping("/cabinet/personal-information")
    public String personal_information(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        model.addAttribute("user", proxyUserDAO.show(auth.getName()));

        return "personal-information";
    }

    @GetMapping("/cabinet/personal-information/edit1")
    public String personal_information_edit1(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        model.addAttribute("user",  proxyUserDAO.show(auth.getName()));

        return "personal-information_edit_1";
    }

    @RequestMapping("/cabinet/personal-information/edit1")
    public String personal_information_edit1(@RequestParam String name, @RequestParam String surname, @RequestParam String patronymic,
                                             @RequestParam String telephone, @RequestParam String city, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        proxyUserDAO.update_user_info(auth.getName(), new UserBuilderIMpl().setName(name).setSurname(surname).setPatronymic(patronymic)
                .setCity(city).setTelephone(telephone).build());

        return "redirect:/cabinet/personal-information";
    }

    @GetMapping("/cabinet/personal-information/edit2")
    public String personal_information_edit2(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        model.addAttribute("user",  proxyUserDAO.show(auth.getName()));

        return "personal-information_edit_2";
    }

    @RequestMapping("/cabinet/personal-information/edit2")
    public String personal_information_edit2(@RequestParam String login, @RequestParam String password, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        proxyUserDAO.setRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        proxyUserDAO.update_user_security(auth.getName(), new UserBuilderIMpl().setLogin(login).setPassword(password).build());

        return "redirect:/logout";
    }

}
