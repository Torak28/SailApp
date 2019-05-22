from flask import Flask, request, jsonify, make_response
from flask_restplus import Api, Resource, reqparse, fields
from flask_cors import CORS, cross_origin
from backend.login_register_delete import *
import backend.user as user

from flask_jwt_extended import JWTManager, create_access_token, create_refresh_token, jwt_required, decode_token, get_jwt_identity

app = Flask(__name__)
app.config['BUNDLE_ERRORS'] = True
app.config['JWT_SECRET_KEY'] = 'jwt-secret-string'
api = Api(app=app, doc='/docs')
jwt = JWTManager(app)

ns_gear = api.namespace('gear', description='Operations on gear')
ns_accounts = api.namespace('accounts', description='Operations involving accounts - registration, login.')
ns_user = api.namespace('user', description='Endpoints involving user')
ns_owner = api.namespace('owner', description='Endpoints involving owner')
ns_admin = api.namespace('admin', description='Endpoints involving admin')


@ns_accounts.route('/register')
class RegisterUser(Resource):
    resource_fields = api.model('registerUser', {
        'first_name': fields.String,
        'last_name': fields.String,
        'email': fields.String,
        'password': fields.String,
        'phone_number': fields.String,
        'role': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('first_name', type=str, required=True, help='First name of the user, e.g. John.')
    parser.add_argument('last_name', type=str, required=True, help='Last name of the user, e.g. Doe.')
    parser.add_argument('email', type=str, required=True, help='E-mail of the user, e.g. john.doe@gmail.com.')
    parser.add_argument('password', type=str, required=True, help='User password in plaintext.')
    parser.add_argument('phone_number', type=str, required=True, help='User phone number.')
    parser.add_argument('role', type=str, required=True, help='User role: user/owner.')

    @api.doc(body=resource_fields)
    def post(self):
        args = self.parser.parse_args(strict=True)
        is_registration_successful = register_new_person(args['first_name'], args['last_name'], args['email'],
                                                         args['password'], args['phone_number'], role=args['role'])
        if is_registration_successful:
            return {'message': 'Registered successfully.'}, 200
        return {'message': 'Failed to register the user. User already exists or bad role given.'}, 401


@ns_accounts.route('/login')
class UserLogin(Resource):
    resource_fields = api.model('loginUser', {
        'email': fields.String,
        'password': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('email', type=str, required=True, help='Email of the user logging in.')
    parser.add_argument('password', type=str, required=True, help='Password of the user logging in.')

    @api.doc(body=resource_fields)
    def post(self):
        args = self.parser.parse_args(strict=True)
        access_token, refresh_token = login_user(args['email'], args['password'])
        if access_token:
            return {
                       'access_token': access_token,
                       'refresh_token': refresh_token
                   }, 200
        else:
            return {'message': 'Login not successful.'}, 401


@ns_accounts.route('/changeData')
class ChangeData(Resource):
    resource_fields = api.model('changeData', {
        'first_name': fields.String,
        'last_name': fields.String,
        'email': fields.String,
        'phone_number': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('first_name', type=str, required=True, help='First name of the user, e.g. John.')
    parser.add_argument('last_name', type=str, required=True, help='Last name of the user, e.g. Doe.')
    parser.add_argument('email', type=str, required=True, help='E-mail of the user, e.g. john.doe@gmail.com.')
    parser.add_argument('phone_number', type=str, required=True, help='User phone number.')

    @api.doc(body=resource_fields)
    @jwt_required
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        kwargs['id'] = user_id
        user_object = user.get_user_by_id(user_id)
        if user_object.email == kwargs['email']:
            kwargs.pop('email')
            user.update_user(kwargs)
            return {'message': 'Data was successfully changed.'}, 200
        elif user_object.email != kwargs['email'] and not user.is_user_in_database_by_mail(kwargs['email']):
            user.update_user(kwargs)
        else:  # mail changed to one already existing in db
            return {'message': 'Email is already taken. Try with another one.'}, 409


@ns_accounts.route('/changePassword')
class ChangePassword(Resource):
    resource_fields = api.model('changePassword', {
        'password': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('password', type=str, required=True, help='New password for the user.')

    @api.doc(body=resource_fields)
    @jwt_required
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        kwargs['id'] = user_id
        change_password(kwargs)
        return {'message': 'Password changed successfully.'}, 200


@ns_owner.route('/addGear')  # TODO: rethink how to write a JOIN.
class AddGear(Resource):
    resource_fields = api.model('addGear', {
        'centre_id': fields.Integer,
        'gear_name': fields.String,
        'gear_price': fields.Integer,
        'gear_quantity': fields.Integer
    })
    parser = reqparse.RequestParser()
    parser.add_argument('centre_id', type=int, required=True, help='Centre ID.')
    parser.add_argument('gear_name', type=str, required=True, help='Name of the gear.')
    parser.add_argument('gear_price', type=int, required=True, help='Price of the gear per hour.')
    parser.add_argument('gear_quantity', type=int, required=True, help='Quantity of added gear.')

    @jwt_required
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id):
            return 'temp answer', 200
        else:
            return {'message': 'Permission denied. You are not the owner.'}, 403


@ns_accounts.route('/getUserData')
class GetUserData(Resource):
    @jwt_required
    def get(self):
        user_id = get_jwt_identity()
        user_obj = user.get_user_by_id(user_id)
        user_data = {'first_name': user_obj.first_name,
                     'last_name': user_obj.last_name,
                     'email': user_obj.email,
                     'phone_number': user_obj.phone_number}
        return jsonify(user_data)


@app.route('/addGearType', methods=['POST'])
@cross_origin(supports_credentials=True)
def add_gear_type():
    r = request.form
    try:
        name = r.get('name')
        price = r.get('price')
        centre_id = r.get('centre_id')
        gear_type_add(centre_id, name, price)
        return "ok"
    except Exception:
        return "error in add_gear_type()"


@app.route('/deleteGearType', methods=['POST'])
@cross_origin(supports_credentials=True)
def delete_gear_type():
    r = request.form
    try:
        name = r.get('name')
        centre_id = r.get('centre_id')
        gear_type_delete(centre_id, name)
        return "ok"
    except Exception:
        return "error in delete_gear_type()"


@app.route('/addGear', methods=['POST'])
@cross_origin(supports_credentials=True)
def add_gear():
    r = request.form
    try:
        quantity = r.get('quantity')
        centre_id = r.get('centre_id')
        name = r.get('name')
        gear_add(centre_id, name, quantity)
        return "ok"
    except Exception:
        return "error in add_gear()"


@app.route('/deleteGear', methods=['POST'])
@cross_origin(supports_credentials=True)
def delete_gear():
    r = request.form
    try:
        quantity = r.get('quantity')
        centre_id = r.get('centre_id')
        name = r.get('name')
        gear_delete(centre_id, name, quantity)
        return "ok"
    except Exception:
        return "error in delete_gear()"


# dla ownera
@app.route('/getGearOwner', methods=['GET'])
@cross_origin(supports_credentials=True)
def owner_get_gear():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        gear_id = r.get('gear_id')
        get_gear_owner(centre_id, gear_id)
        return "ok"
    except Exception:
        return "error in owner_get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)
def owner_get_all_gear():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        all_gear_owner(centre_id)
        return "ok"
    except Exception:
        return "error in owner_get_all_gear()"


# client panel
@app.route('/changeDataClient', methods=['POST'])
@cross_origin(supports_credentials=True)
def data_change():            # bierzemy id bo nie mozemy usera o to pytac
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        phone_number = r.get('phone_number')
        password = r.get('password')
        user_id=r.get('user_id')
        change_data(user_id, first_name, last_name, email, phone_number, password)
        return "ok"
    except Exception:
        return "error in data_change()"


@app.route('/getGearClient', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_gear():
    r = request.form
    try:
        gear_id = r.get('gear_id')
        get_gear_client(gear_id)
        return "ok"
    except Exception:
        return "error in get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_all_gear():
    try:
        all_gear_client()
        return "ok"
    except Exception:
        return "error in get_all_gear()"


@app.route('/rentGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def rent_gear():
    r = request.form
    try:
        user_id = r.get('user_id')
        centre_id = r.get('centre_id')
        gear_id = r.get('gear_id')
        start = r.get('start')
        end = r.get('end')
        rent_amount = r.get('rent_amount')
        gear_rent(user_id, gear_id, centre_id, start, end, rent_amount)
        return "ok"
    except Exception:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in rent_gear()"


@app.route('/getCentres', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def centres_get():
    try:
        get_all_centres()
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in centres_get()"


@app.route('/getAllCentreGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def all_centre_gear_get():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        get_all_centre_gear(centre_id)
        return "ok"
    except Exception:
        return "error in all_centre_gear_get()"


@app.route('/getAllUserRentals', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def all_user_rentals_get():
    r = request.form
    try:
        user_id = r.get('user_id')
        get_all_user_rentals(user_id)
        return "ok"
    except Exception:
        return "error in all_centre_gear_get()"


if __name__ == '__main__':
    app.run(debug=True)
