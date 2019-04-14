from sqlalchemy import Column, Integer, String, Date, Sequence, ForeignKey
from sqlalchemy.orm import relationship, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import create_engine
# import datetime

Base = declarative_base()
DATABASE_URI = 'postgres+psycopg2://postgres:12345@localhost:5432/db.py'
engine = create_engine(DATABASE_URI)
Session = sessionmaker(bind=engine)
s = Session()


def recreate_database():
    Base.metadata.drop_all(engine)
    Base.metadata.create_all(engine)


class User(Base):
    __tablename__ = 'user'

    user_id_seq = Sequence('cart_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                user_id_seq,
                server_default=user_id_seq.next_value(),
                primary_key=True,
                unique=True)
    first_name = Column(String)
    last_name = Column(String)
    email = Column(String,
                   unique=True)
    password = Column(String)
    phone_number = Column(String)
    role_id = Column(Integer, ForeignKey('role.id'))
    auth_token = Column(String)
    gear_rental = relationship("GearRental")
    class_table = relationship("Class")
    owner = relationship('WaterCentre')


class Role(Base):
    __tablename__ = 'role'

    role_id_seq = Sequence('role_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                role_id_seq,
                server_default=role_id_seq.next_value(),
                primary_key=True,
                unique=True)
    role_name = Column(String)

    role = relationship("User")


class Gear(Base):
    __tablename__ = 'gear'

    gear_id_seq = Sequence('gear_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                gear_id_seq,
                server_default=gear_id_seq.next_value(),
                primary_key=True,
                unique=True)
    centre_id = Column(Integer, ForeignKey('centre.id'))
    name = Column(String)
    total_quantity = Column(Integer)
    price_hour = Column(Integer)

    gear = relationship('GearRental')


class GearRental(Base):
    __tablename__ = 'gear_rental'

    gear_rental_id_seq = Sequence('gear_rental_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                gear_rental_id_seq,
                server_default=gear_rental_id_seq.next_value(),
                primary_key=True,
                unique=True)
    user_id = Column(Integer, ForeignKey('user.id'))
    gear_id = Column(Integer, ForeignKey('gear.id'))
    centre_id = Column(Integer, ForeignKey('centre.id'))
    rent_start = Column(Date)
    rent_end = Column(Date)
    rent_amount = Column(Integer)


class WaterCentre(Base):
    __tablename__ = 'centre'

    centre_rental_id_seq = Sequence('centre_rental_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                centre_rental_id_seq,
                server_default=centre_rental_id_seq.next_value(),
                primary_key=True,
                unique=True)
    name = Column(String)
    owner_id = Column(Integer, ForeignKey('user.id'))
    latitude = Column(String)
    longitude = Column(String)

    gear = relationship("Gear")
    gear_rental = relationship("GearRental")


class Class(Base):
    __tablename__ = 'class'

    class_rental_id_seq = Sequence('class_rental_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                class_rental_id_seq,
                server_default=class_rental_id_seq.next_value(),
                primary_key=True,
                unique=True)
    class_type_id = Column(Integer, ForeignKey('class_type.id'))
    user_id = Column(Integer, ForeignKey('user.id'))
    class_date = Column(Date)


class ClassType(Base):
    __tablename__ = 'class_type'

    class_type_rental_id_seq = Sequence('class_type_rental_id_seq', metadata=Base.metadata)

    id = Column(Integer,
                class_type_rental_id_seq,
                server_default=class_type_rental_id_seq.next_value(),
                primary_key=True,
                unique=True)
    class_type = Column(String)

    class_table = relationship('Class')


# recreate_database()
