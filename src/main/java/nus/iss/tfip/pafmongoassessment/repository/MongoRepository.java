package nus.iss.tfip.pafmongoassessment.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import nus.iss.tfip.pafmongoassessment.Constants;
import nus.iss.tfip.pafmongoassessment.model.Transfer;

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

    /*
     * db.accounts.updateOne(
     * { "account_id": "uFSFRqUpJy" },
     * { $set: {"balance": Double(900.00)}}
     * )
     */
    // public void withdrawFunds(Transfer transfer) throws Exception {
    // Double originalBalance =
    // this.getBalance(transfer.getFromAccount()).getDouble(FIELD_BALANCE);

    // Criteria criteria =
    // Criteria.where(FIELD_ACCOUNT_ID).is(transfer.getFromAccount());
    // Query query = new Query(criteria);
    // UpdateDefinition ud = new Update()
    // .set(FIELD_BALANCE, (originalBalance - transfer.getAmount()));
    // Long modifiedRows = template.updateFirst(query, ud,
    // COLLECTION_ACCOUNTS).getModifiedCount();
    // if (modifiedRows != 1) {
    // throw new Exception("Error withdrawing funds");
    // };
    // }

    // public void depositFunds(Transfer transfer) throws Exception {
    // Double originalBalance =
    // this.getBalance(transfer.getToAccount()).getDouble(FIELD_BALANCE);

    // Criteria criteria =
    // Criteria.where(FIELD_ACCOUNT_ID).is(transfer.getToAccount());
    // Query query = new Query(criteria);
    // UpdateDefinition ud = new Update()
    // .set(FIELD_BALANCE, (originalBalance + transfer.getAmount()));
    // Long modifiedRows = template.updateFirst(query, ud,
    // COLLECTION_ACCOUNTS).getModifiedCount();
    // if (modifiedRows != 1) {
    // throw new Exception("Error withdrawing funds");
    // }
    // }

    /*
     * db.accounts.updateOne(
     * { account_id: "ckTV56axff" },
     * { $inc: { "balance": -12 } }
     * )
     */
    public void withdrawFunds(Transfer transfer) throws Exception {
        Criteria criteria = Criteria.where(FIELD_ACCOUNT_ID).is(transfer.getFromAccount());
        Query query = new Query(criteria);
        Update updateOps = new Update().inc(FIELD_BALANCE, -(transfer.getAmount()));
        UpdateResult updateResult = template.updateFirst(query, updateOps, Document.class, COLLECTION_ACCOUNTS);
        Long modifiedRows = updateResult.getModifiedCount();
        if (modifiedRows != 1) {
            throw new Exception("Error withdrawing funds");
        }
    }

    public void depositFunds(Transfer transfer) throws Exception {
        Criteria criteria = Criteria.where(FIELD_ACCOUNT_ID).is(transfer.getFromAccount());
        Query query = new Query(criteria);
        Update updateOps = new Update().inc(FIELD_BALANCE, transfer.getAmount());
        UpdateResult updateResult = template.updateFirst(query, updateOps, Document.class, COLLECTION_ACCOUNTS);
        Long modifiedRows = updateResult.getModifiedCount();
        if (modifiedRows != 1) {
            throw new Exception("Error depositing funds");
        }
    }

    public Boolean logTransaction(Document doc) {
        Document response = template.insert(doc, COLLECTION_LOGS);
        // System.out.println(response);
        return response.getObjectId(FIELD_OBJ_ID) != null;
    }
}
