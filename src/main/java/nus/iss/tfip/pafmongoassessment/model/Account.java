package nus.iss.tfip.pafmongoassessment.model;

import org.bson.Document;
import lombok.Data;
import nus.iss.tfip.pafmongoassessment.Constants;

@Data
public class Account implements Constants {
    private String account_id;
    private String name;
    private Double balance;

    public static Account create(Document doc) {
        Account acc = new Account();
        acc.setAccount_id(doc.getString(FIELD_ACCOUNT_ID));
        acc.setName(doc.getString(FIELD_NAME));
        acc.setBalance(doc.getDouble(FIELD_BALANCE));   
        return acc;
    }
}
