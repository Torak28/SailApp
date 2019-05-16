# Setup Database

In progress!

![alt text](https://flask-restplus.readthedocs.io/en/stable/#)

### Schemat bazy danych

![alt text](schemat_bazki.png)


### First of all update your machine:

```
sudo apt-get update
sudo apt-get upgrade
```

### Then install all programs:

```
sudo apt-get install postgresql
sudo apt-get install git
sudo apt-get install python3
sudo apt-get install python3-pip
sudo apt-get install python3-dev postgresql-contrib python3-psycopg2 libpq-dev
```

### Set PostgresSQL:

```
sudo -u postgres psql --command "create database SailAppDB;"
sudo -u postgres psql --command "create user docker with encrypted password 'docker';"
sudo -u postgres psql --command "grant all privileges on database SailAppDB to docker;"
```

### Copy repo from git:

```
mkdir app
cd app
git clone https://github.com/Torak28/SailApp.git
cd SailApp/database
```

### Install all python dependencies:

```
pip3 install -r requirements.txt
```

### Export your os variable:

```
export DATABASE_URL='postgres+psycopg2://docker:docker@localhost:5432/sailappdb'
export DATABASE_URL='postgres+psycopg2://postgres:12345@localhost:5432/sailappdb'
```

### Create db:

```
python3 create_db.py
```

Check by running:

```
sudo -u postgres psql sailappdb
```

If it's working You are all good!

### Create Tables:

```
python3 database_classes.py
```

And check it by running:

```
sudo -u postgres psql sailappdb
\dt
```
You should see list of all your tables.

### Now check if adding works, by:

```
python prepare_db.py
```

And check it by running:

```
sudo -u postgres psql sailappdb
SELECT * FROM user_account;
```

You should see 2 new users!

:fire: