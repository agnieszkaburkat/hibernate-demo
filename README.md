# hibernate-demo

Simple project showing how @Transactional works and also comparing different types of batch inserts into postgreSQL db

The conclusion is:
```
---------------------------------------------
ns         %     Task name
---------------------------------------------
8333421374  088%  Hibernate saveAll()
488026071  005%  CRUD Hibernate saveAll()
274627939  003%  JDBC Template save with Executor Service
375936418  004%  JDBC Template save
```

meaning the optimal way is to use either JDBCTemplate and write manually queries remembering to persist any joined entities before batchUpdate() call or use Hibernate but instead of long type id use UUID
