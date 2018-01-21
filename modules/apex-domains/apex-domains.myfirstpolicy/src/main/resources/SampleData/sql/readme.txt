To set up the apex_sales database, simply run the following command:
psql -U postgres -a -f apex_sales.sql

To test if the database has been set up correctly, rin the psql client as follows:

psql -U postgres -d apex_sales
select * from sales;
select * from items;
select * from assistants;
select * from branches;

