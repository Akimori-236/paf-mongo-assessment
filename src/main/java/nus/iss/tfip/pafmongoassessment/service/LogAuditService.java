package nus.iss.tfip.pafmongoassessment.service;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.tfip.pafmongoassessment.repository.MongoRepository;
import nus.iss.tfip.pafmongoassessment.Constants;
import nus.iss.tfip.pafmongoassessment.model.Transfer;

@Service
public class LogAuditService implements Constants {
    @Autowired
    private MongoRepository mongoRepo;

    /*
     * db.logs.insertOne({
     * transactionId: "abcd1234",
     * date: new Date(),
     * from_account: "V9L3Jd1BBI",
     * to_account: "fhRq46Y6vB",
     * amount: Double(10.00)
     * })
     */
    public Boolean logTransaction(Transfer transfer) {
        Document doc = new Document();
        doc.put(FIELD_TRANSACTION_ID, transfer.getId());
        doc.put(FIELD_DATE, new Date());
        doc.put(FIELD_FROM_ACCOUNT, transfer.getFromAccount());
        doc.put(FIELD_TO_ACCOUNT, transfer.getToAccount());
        doc.put(FIELD_AMOUNT, transfer.getAmount());
        return mongoRepo.logTransaction(doc);
    }

    public List<Document> getAllLogs() {
        return mongoRepo.getAllLogs();
    }
}
