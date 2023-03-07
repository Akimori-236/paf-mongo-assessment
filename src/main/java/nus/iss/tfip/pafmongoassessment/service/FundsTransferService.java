package nus.iss.tfip.pafmongoassessment.service;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.tfip.pafmongoassessment.model.Account;
import nus.iss.tfip.pafmongoassessment.repository.MongoRepository;

import nus.iss.tfip.pafmongoassessment.Constants;
@Service
public class FundsTransferService implements Constants {

    @Autowired
    private MongoRepository mongoRepo;

    public List<Account> getAllAccounts() {
        List<Document> docList = mongoRepo.getAllAccounts();
        List<Account> accList = docList.stream()
                                .map(doc -> Account.create(doc))
                                .toList();
        return accList;
    }

    public Boolean isAccountExist(String account_id) {
        return mongoRepo.isAccountExist(account_id);
    }

    public Double getBalance(String account_id) {
        Document doc = mongoRepo.getBalance(account_id);
        return doc.getDouble(FIELD_BALANCE);
    }
}
