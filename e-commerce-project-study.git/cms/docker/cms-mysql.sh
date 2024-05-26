docker run -d \
--name cms-mysql \
-e MYSQL_ROOT_PASSWORD="cms" \
-e MYSQL_USER="cms" \
-e MYSQL_PASSWORD="cms" \
-e MYSQL_DATABASE="cms" \
-p 3306:3306 \
mysql:latest