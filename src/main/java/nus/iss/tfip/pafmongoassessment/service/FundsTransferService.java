package nus.iss.tfip.pafmongoassessment.service;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.tfip.pafmongoassessment.model.Account;
import nus.iss.tfip.pafmongoassessment.repository.MongoRepository;

import nus.iss.tfip.pafmongoassessment.Constants;
import nus.iss.tfip.pafmongoassessment.model.Transfer;

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

    @Transactional(rollbackFor = Exception.class)
    public Transfer startTransfer(Transfer transfer) throws Exception {
        // generate transaction id
        String txnId = UUID.randomUUID().toString().substring(0, 8);
        transfer.setId(txnId);

        mongoRepo.withdrawFunds(transfer);
        mongoRepo.depositFunds(transfer);
        return transfer;
    }
}
