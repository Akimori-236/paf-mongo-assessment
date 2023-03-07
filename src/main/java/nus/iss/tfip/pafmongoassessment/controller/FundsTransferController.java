package nus.iss.tfip.pafmongoassessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import nus.iss.tfip.pafmongoassessment.model.Account;
import nus.iss.tfip.pafmongoassessment.model.Transfer;
import nus.iss.tfip.pafmongoassessment.service.FundsTransferService;

@Controller
@RequestMapping(produces=MediaType.TEXT_HTML_VALUE)
public class FundsTransferController {

    @Autowired
    private FundsTransferService fundsSvc;

    @GetMapping(path = { "/", "index.html" })
    public String landingPage(Model model) {
        List<Account> accountList = fundsSvc.getAllAccounts();
        Transfer transfer = new Transfer();
        model.addAttribute("transfer", transfer);
        model.addAttribute("accountList", accountList);
        return "view0";  
    }

    @PostMapping(path = {"/transfer", "/transfer/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String startTransfer(@Valid Transfer transfer, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            // System.err.println(binding.getAllErrors().get(0).getDefaultMessage().toString());
            // List<Account> accountList = fundsSvc.getAllAccounts();
            model.addAttribute("transfer", transfer);
            // model.addAttribute("accountList", accountList);
            return "view0";
        }

        return "view1";
    }

}
