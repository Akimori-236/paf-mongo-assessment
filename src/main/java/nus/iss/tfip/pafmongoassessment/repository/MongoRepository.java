package nus.iss.tfip.pafmongoassessment.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import nus.iss.tfip.pafmongoassessment.Constants;

@Repository
public class MongoRepository implements Constants {

    @Autowired
    private MongoTemplate template;

    // db.accounts.find()
    // SELECT * FROM accounts;
    public List<Document> getAllAccounts() {
        Query query = new Query();
        List<Document> docList = template.find(query, Document.class, COLLECTION_ACCOUNTS);
        return docList;
    }

}
