package com.qt.caesar.common;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 这里提供两种ID生成方式:1:利用redis单线程2:mongo的findAndModify
 *
 * Created by renfei on 17/5/11.
 */
@Component
public class IdGenerator implements org.springframework.util.IdGenerator{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INCR_KEY = "id.generator";

    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }

    /**
     * 集合类型,全局id生成:GLOBAL
     */
    public interface CollectionType{
        String GLOBAL = "global";
    }


    @Document(collection = "ids")
    public class IdBean{
        @Id
        private String id;
        private Long value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }

    /**
     * 通过mongo的findAndModify生成id
     * @param collectionName 对应mongo collection,如果为null或者"" 或者 GLOBAL 则使用全局id
     * @return
     */
    public Long getIdByMongo(String collectionName){
        long incr = 1;

        collectionName = getCollection(collectionName);
        Query query = new Query(Criteria.where("_id").is(collectionName));
        Update update = new Update().inc("value", incr);
        IdBean idBean = mongoTemplate.findAndModify(query, update, IdBean.class);

        if(idBean == null){
            IdBean newIdBean = new IdBean();
            newIdBean.setId(collectionName);
            newIdBean.setValue(incr);
            mongoTemplate.insert(newIdBean);
        }else{
            incr = idBean.getValue();

            long id = this.getIdFromMysql(collectionName, "MongoDB");
            if(id > incr){
                update = new Update().set("value", incr + 1);
                idBean = mongoTemplate.findAndModify(query, update, IdBean.class);

                incr = idBean.getValue();
            }

        }

        //同步到mysql
        this.saveIdMysql(collectionName, incr, "MongoDB");

        return incr;
    }

    /**
     * 通过redis HINCRB 生成id
     * @param collectionName 对应redis key,如果为null或者"" 或者 GLOBAL 则使用全局id
     * @return
     */
    public Long getIdByRedis(String collectionName){
        collectionName = getCollection(collectionName);

        long id = stringRedisTemplate.opsForHash().increment(INCR_KEY, collectionName, 1);

        long idMysql = this.getIdFromMysql(collectionName, "Redis");
        if(idMysql > id){
            long newId = idMysql + 1;
            stringRedisTemplate.opsForHash().put(INCR_KEY, collectionName, String.valueOf(newId));

            id = newId;
        }

        //同步到mysql
        this.saveIdMysql(collectionName, id, "Redis");

        return id;
    }

    /**
     * 不能表中maxid,以后分表就完犊子了
     *
     * @return
     */
    private Long getIdFromMysql(String collectionName, String dbType){
        String sql = "select id, `value` from id_generator where collection = ? and db_type = ? limit 1";

        IdBean idBean = null;
        try{

            idBean = jdbcTemplate.queryForObject(sql, new Object[]{collectionName, dbType}, (resultSet, i) -> {
                IdBean idBean1 = new IdBean();
                idBean1.setId(resultSet.getString("id"));
                idBean1.setValue(resultSet.getLong("value"));
                return idBean1;
            });
        }catch (EmptyResultDataAccessException e){
            logger.info("id_generator is empty", e);
        }

        if(idBean != null){
            return idBean.getValue();
        }else{
            return Long.MIN_VALUE;
        }
    }

    /**
     * 同步id
     * @param collectionName
     * @param maxValue
     * @param dbType
     */
    private void saveIdMysql(String collectionName, long maxValue, String dbType){
        String sql = "insert into id_generator(collection, `value`, update_time, db_type) values(?,?,?,?) ON DUPLICATE KEY UPDATE `value`=?";

        jdbcTemplate.update(sql, collectionName, maxValue, System.currentTimeMillis(), dbType, maxValue);
    }

    private String getCollection(String collectionName){
        if(StringUtils.isEmpty(collectionName) || Objects.equal(CollectionType.GLOBAL, collectionName)){
            collectionName = CollectionType.GLOBAL;
        }
        return collectionName;
    }

}
