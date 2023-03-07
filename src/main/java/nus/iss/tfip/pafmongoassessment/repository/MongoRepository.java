package nus.iss.tfip.pafmongoassessment.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
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

    // db.accounts.find({ "account_id": "???" });
    public Boolean isAccountExist(String account_id) {
        Criteria criteria = Criteria.where(FIELD_ACCOUNT_ID).is(account_id);
        Query query = new Query(criteria);
        Boolean isExist = template.exists(query, COLLECTION_ACCOUNTS);
        return isExist;
    }

    /*
     * db.accounts.aggregate([
     * { $match: { "account_id": "if9l185l18" } },
     * {
     * $project: {
     * _id: 0,
     * balance: 1
     * }
     * }
     * ])
     */
    public Document getBalance(String account_id) {
        Criteria criteria = Criteria.where(FIELD_ACCOUNT_ID).is(account_id);
        MatchOperation matchAccId = Aggregation.match(criteria);
        ProjectionOperation getBalance = Aggregation.project(FIELD_BALANCE)
                .andExclude(FIELD_OBJ_ID);
        Aggregation pipeline = Aggregation.newAggregation(matchAccId, getBalance);
        AggregationResults<Document> results = template.aggregate(
                pipeline, COLLECTION_ACCOUNTS, Document.class);
        return results.getUniqueMappedResult();
    }
}
