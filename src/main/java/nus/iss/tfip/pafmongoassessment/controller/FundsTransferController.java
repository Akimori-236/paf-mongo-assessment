package nus.iss.tfip.pafmongoassessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import nus.iss.tfip.pafmongoassessment.model.Account;
import nus.iss.tfip.pafmongoassessment.model.Transfer;
import nus.iss.tfip.pafmongoassessment.service.FundsTransferService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class FundsTransferController {

    @Autowired
    private FundsTransferService fundsSvc;

    @GetMapping(path = { "/", "index.html" })
    public String landingPage(Model model) {
        List<Account> accountList = fundsSvc.getAllAccounts();
        Transfer transfer = new Transfer();
        transfer.setFromAccount("");
        model.addAttribute("transfer", transfer);
        model.addAttribute("accountList", accountList);
        return "view0";
    }

    @PostMapping(path = { "/transfer", "/transfer/" }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String startTransfer(@Valid Transfer transfer, BindingResult binding, Model model) throws Exception {
        List<Account> accountList = fundsSvc.getAllAccounts();
        if (binding.hasErrors()) {
            // System.err.println(binding.getAllErrors().get(0).getDefaultMessage().toString());
            model.addAttribute("transfer", transfer);
            model.addAttribute("accountList", accountList);
            return "view0";
        }

        // C0 both acc should exist
        if (!fundsSvc.isAccountExist(transfer.getFromAccount())) {
            ObjectError objErr = new ObjectError("errMsg", "From Account does not exist");
            binding.addError(objErr);
        }
        if (!fundsSvc.isAccountExist(transfer.getToAccount())) {
            ObjectError objErr = new ObjectError("errMsg", "To Account does not exist");
            binding.addError(objErr);
        }
        // C2
        if (transfer.getFromAccount().equals(transfer.getToAccount())) {
            ObjectError objErr = new ObjectError("errMsg", "From Account & To Account is the same");
            binding.addError(objErr);
        }
        // C5 should have sufficient funds
        if (transfer.getAmount() > fundsSvc.getBalance(transfer.getFromAccount())) {
            ObjectError objErr = new ObjectError("errMsg", "Insufficient balance in From Account");
            binding.addError(objErr);
        }
        if (binding.hasErrors()) {
            model.addAttribute("transfer", transfer);
            model.addAttribute("accountList", accountList);
            return "view0";
        }
        // SUCCESS
        transfer = fundsSvc.startTransfer(transfer);
        model.addAttribute("transfer", transfer);
        return "view1";
    }

}
