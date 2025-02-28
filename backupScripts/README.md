docker exec -it peruHCE-db-replic bash


mariabackup --backup --target-dir=/backup --user=root --password='yourpassword'




mariabackup --backup --target-dir=/var/mariadb/backup/ --user=mariabackup --password=mypassword






mariabackup --prepare --target-dir=/backup