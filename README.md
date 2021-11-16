sudo -u postgres psql  
- \q
- CREATE DATABASE vmedb_local;
- CREATE USER vmeuser WITH PASSWORD 'Pass!234;
- GRANT ALL PRIVILEGES ON DATABASE vmedb_local TO vmeuser;