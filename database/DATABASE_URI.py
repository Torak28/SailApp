import os

try:
    DATABASE_URI = os.environ['DATABASE_URL']
except KeyError:
    DATABASE_URI = 'postgres+psycopg2://postgres:12345@localhost:5432/sailappdb'
    # DATABASE_URI = 'postgres://gmrncudmfjujkg:b3993c9ef279d959afeb864048b069e486577d01695e44cb604859a7c4427987@ec2-54-75-245-196.eu-west-1.compute.amazonaws.com:5432/dar41jmah17m6l'
