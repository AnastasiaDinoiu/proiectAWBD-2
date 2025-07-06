### Start db for test and prod
```bash
docker-compose build
docker-compose up -d
```

#### Test connection:
```bash
mysql -u test_user -p -h 127.0.0.1 -P 3307 test_db
```

```bash
mysql -u prod_user -p -h 127.0.0.1 -P 3308 prod_db
```

#### Grant privileges
```
docker exec -it mysql-test bash
nano /etc/mysql/mysql.conf.d/mysqld.cnf
```

### Start the server

dev app:
```bash
clear && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

test app:
```bash
clear && ./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

prod app:
```bash
clear && ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```