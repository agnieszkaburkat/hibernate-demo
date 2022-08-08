# hibernate-demo

Simple project showing how @Transactional works and also comparing different types of batch inserts into postgreSQL db

The conclusion is (when inserting 10_000 products per each method):
```
---------------------------------------------
ns         %     Task name
---------------------------------------------
7899616085  088%  Hibernate saveAll()
443045675  005%  UUID Hibernate saveAll()
241366080  003%  JDBC Template save with Executor Service
351577335  004%  JDBC Template save

2022-08-01 21:06:42.256  INFO 24108 --- [nio-8080-exec-1] i.StatisticalLoggingSessionEventListener : Session Metrics {
    1553478 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    19526654 nanoseconds spent preparing 10009 JDBC statements;
    6951768437 nanoseconds spent executing 10003 JDBC statements;
    666270378 nanoseconds spent executing 404 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    0 nanoseconds spent performing 0 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
    976417269 nanoseconds spent executing 6 flushes (flushing a total of 110017 entities and 0 collections);
    0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
```

meaning the optimal way is to use either JDBCTemplate and write manually queries remembering to persist any joined entities before batchUpdate() call or use Hibernate but instead of long type id use UUID
