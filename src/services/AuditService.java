package services;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import utils.ClassNameUtil;
import utils.PropertyLoader;
import utils.TestEntity;


public class AuditService {

    private TestEntity entity;
    private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());
    private static final String AUDIT_URL = PropertyLoader.loadProperty("audit.url");
    private static final String FETCH_ALL_TYPES_URL = AUDIT_URL + "audit/logs/types";
    private static final String FETCH_ALL_TYPE_URL = AUDIT_URL + "audit/logs/";

    public AuditService(TestEntity entity) {
        this.entity = entity;
    }

    private JsonObject fetchAllTypes() {
        entity.stringBuilder.append("Fetch all types from audit service <br>");
        log.info("Fetch all types from audit service");
        return (JsonObject) entity.client
                .get(FETCH_ALL_TYPES_URL, 200)
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .execute();
    }

    private JsonObject fetchAllType() {
        entity.stringBuilder.append("Fetch all type from audit service by uuid ").append(entity.player.getUuid()).append("<br>");
        log.info("Fetch all type from audit service by uuid " + entity.player.getUuid());
        return (JsonObject) entity.client
                .get(FETCH_ALL_TYPE_URL + entity.player.getUuid() + "/types", 200)
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .execute();
    }



}
