from flask import Flask, request, jsonify
from flask_restplus import Api, Resource, reqparse, fields
from flask_cors import CORS, cross_origin
from backend.login_register_delete import *
from database.prepare_db import prepare_db
import backend.user as user
import backend.water_centre as wc
import backend.gear as gear
import backend.rentals as rental

from flask_jwt_extended import JWTManager, jwt_required, get_jwt_identity, jwt_refresh_token_required

app = Flask(__name__)
app.config['BUNDLE_ERRORS'] = True
app.config['JWT_SECRET_KEY'] = 'jwt-secret-string'
api = Api(app=app, doc='/docs')
jwt = JWTManager(app)
jwt._set_error_handler_callbacks(api)


ns_gear = api.namespace('gear', description='Operations on gear.')
ns_accounts = api.namespace('accounts', description='Operations involving accounts - registration, login.')
ns_user = api.namespace('user', description='Endpoints involving user.')
ns_owner = api.namespace('owner', description='Endpoints involving owner.')
ns_admin = api.namespace('admin', description='Endpoints involving admin.')
ns_rental = api.namespace('rental', description='Endpoints involving renting.')
ns_test = api.namespace('POPULATING_DB', description='Endpoints to populate DB. TEST PURPOSES ONLY.')


@app.route('/helloheroku')
def hello_heroku():
    return "Hello Heroku!"


@ns_test.route('/ClearAndPopulateDb')
class PopulateDb(Resource):
    def get(self):
        prepare_db()

@api.route('/refreshToken')
class RefreshToken(Resource):
    resource_fields = api.model('refreshToken', {
        'access_token': fields.String,
    })

    @api.response(200, 'Refreshed access token.', [resource_fields])
    @jwt_refresh_token_required
    def get(self):
        user_id = get_jwt_identity()
        return {'access_token': create_access_token(identity=user_id)}, 200


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

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @api.response(200, 'Successful registration.')
    @api.response(401, 'User already exists/bad role passed.')
    def post(self):
        args = self.parser.parse_args(strict=True)
        is_registration_successful = register_new_person(args['first_name'], args['last_name'], args['email'],
                                                         args['password'], args['phone_number'], role=args['role'])
        if is_registration_successful:
            return {'msg': 'Registered successfully.'}, 200
        return {'msg': 'Failed to register the user. User already exists or bad role given.'}, 401


@ns_accounts.route('/login')
class UserLogin(Resource):
    resource_fields = api.model('loginUser', {
        'email': fields.String,
        'password': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('email', type=str, required=True, help='Email of the user logging in.')
    parser.add_argument('password', type=str, required=True, help='Password of the user logging in.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @api.response(200, 'Successful login, access_token and refresh_token returned.')
    @api.response(401, 'Login not successful.')
    def post(self):
        args = self.parser.parse_args(strict=True)
        access_token, refresh_token = login_user(args['email'], args['password'])
        if access_token:
            return {
                       'access_token': access_token,
                       'refresh_token': refresh_token
                   }, 200
        else:
            return {'msg': 'Login not successful.'}, 401


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

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Successful data change.')
    @api.response(409, 'Failed because given email is already taken.')
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        kwargs['id'] = user_id
        user_object = user.get_user_by_id(user_id)
        if user_object.email == kwargs['email']:
            kwargs.pop('email')
            user.update_user(kwargs)
        elif user_object.email != kwargs['email'] and not user.is_user_in_database_by_mail(kwargs['email']):
            user.update_user(kwargs)
        else:  # mail changed to one already existing in db
            return {'msg': 'Email is already taken. Try with another one.'}, 409
        return {'msg': 'Data was successfully changed.'}, 200


@ns_accounts.route('/changePassword')
class ChangePassword(Resource):
    resource_fields = api.model('changePassword', {
        'password': fields.String
    })
    parser = reqparse.RequestParser()
    parser.add_argument('password', type=str, required=True, help='New password for the user.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Successful password change.')
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        kwargs['id'] = user_id
        change_password(kwargs)
        return {'msg': 'Password changed successfully.'}, 200


@ns_accounts.route('/getUserData')
class GetUserData(Resource):
    resource_fields = api.model('getUserData', {
        'first_name': fields.String,
        'last_name': fields.String,
        'email': fields.String,
        'phone_number': fields.String,
    })

    @jwt_required
    @api.response(200, 'User data returned successfully.', resource_fields)
    def get(self):
        user_id = get_jwt_identity()
        user_obj = user.get_user_by_id(user_id)
        user_data = {'first_name': user_obj.first_name,
                     'last_name': user_obj.last_name,
                     'email': user_obj.email,
                     'phone_number': user_obj.phone_number}
        return jsonify(user_data)


@ns_owner.route('/addWaterCentre')
class AddWaterCentre(Resource):
    resource_fields = api.model('addWaterCentre', {
        'centre_name': fields.String,
        'latitude': fields.String,
        'longitude': fields.String,
        'phone_number': fields.String,
    })
    parser = reqparse.RequestParser()
    parser.add_argument('centre_name', type=str, required=True, help='New name for the water centre.')
    parser.add_argument('latitude', type=str, required=True, help='Latitude in DDD.dddd format.')
    parser.add_argument('longitude', type=str, required=True, help='Longitude in DDD.dddd format.')
    parser.add_argument('phone_number', type=str, required=True, help='Contact number for the Water Centre.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Water centre added successfully.')
    @api.response(403, 'User does not have the proper rights.')
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id):
            wc.add_water_centre(user_id, kwargs['centre_name'], kwargs['latitude'],
                                kwargs['longitude'], kwargs['phone_number'])
            return {'msg': 'Water Centre added successfully.'}, 200
        return {'msg': 'You do not have the proper rights.'}, 403


@ns_gear.route('/addGear')  # for Owner
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

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Gear added successfully.')
    @api.response(403, 'User does not have the proper rights.')
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id) and user.is_owner_the_centre_owner(user_id, kwargs['centre_id']):
            gear.add_gear(kwargs['centre_id'], kwargs['gear_name'], kwargs['gear_price'], kwargs['gear_quantity'])
            return {'msg': 'Gear added successfully.'}, 200
        return {'msg': 'Permission denied. You are not the owner.'}, 403


@ns_gear.route('/editGear')   # for Owner
class EditGear(Resource):
    resource_fields = api.model('editGear', {
        'centre_id': fields.Integer,
        'gear_id': fields.Integer,
        'gear_price': fields.Integer,
        'gear_quantity': fields.Integer
    })
    parser = reqparse.RequestParser()
    parser.add_argument('centre_id', type=int, required=True, help='Centre ID.')
    parser.add_argument('gear_id', type=int, required=True, help='ID of the gear you wish to change.')
    parser.add_argument('gear_name', type=str, required=True, help='New gear name.')
    parser.add_argument('gear_price', type=int, required=True, help='Price of the gear per hour.')
    parser.add_argument('gear_quantity', type=int, required=True, help='Quantity of the gear.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Gear updated successfully.')
    @api.response(403, 'User does not have the proper rights.')
    def put(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id) and user.is_owner_the_centre_owner(user_id, kwargs['centre_id']):
            gear.update_gear(kwargs['centre_id'], kwargs['gear_id'], kwargs['gear_name'],
                             kwargs['gear_price'], kwargs['gear_quantity'])
            return {'msg': 'Gear updated successfully.'}, 200
        return {'msg': 'Permission denied. You are not the owner.'}, 403


@ns_gear.route('/deleteGear')   # for Owner
class DeleteGear(Resource):
    resource_fields = api.model('editGear', {
        'centre_id': fields.Integer,
        'gear_id': fields.Integer,
    })
    parser = reqparse.RequestParser()
    parser.add_argument('centre_id', type=int, required=True, help='Centre ID.')
    parser.add_argument('gear_id', type=int, required=True, help='ID of the gear you wish to delete.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Gear deleted successfully.')
    @api.response(403, 'User does not have the proper rights.')
    def delete(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id) and user.is_owner_the_centre_owner(user_id, kwargs['centre_id']):
            gear.delete_gear(kwargs['centre_id'], kwargs['gear_id'])
            return {'msg': 'Gear deleted successfully.'}, 200
        return {'msg': 'Permission denied. You are not the owner.'}, 403


@ns_gear.route('/getAllGear/<int:centre_id>')
class GetAllGear(Resource):
    resource_fields = api.model('editGear', {
        'gear_name': fields.String,
        'gear_price': fields.Integer,
        'gear_quantity': fields.Integer,
        'gear_id': fields.Integer
    })

    @api.response(200, 'Data returned successfully.', [resource_fields])
    def get(self, centre_id):
        all_gear = gear.get_all_gear(centre_id)
        return jsonify(all_gear)


@ns_rental.route('/rentGear')
class RentGear(Resource):
    resource_fields = api.model('rentGear', {
        'centre_id': fields.Integer,
        'gear_id': fields.Integer,
        'rent_amount': fields.Integer,
        'rent_start': fields.DateTime,
        'rent_end': fields.DateTime,
    })
    parser = reqparse.RequestParser()
    parser.add_argument('centre_id', type=int, required=True, help='Centre ID.')
    parser.add_argument('gear_id', type=int, required=True, help='ID of the gear you wish to rent.')
    parser.add_argument('rent_amount', type=int, required=True, help='Amount of gear you want to rent.')
    parser.add_argument('rent_start', type=datetime, required=True, help='Start of the rent datetime.')
    parser.add_argument('rent_end', type=datetime, required=True, help='End of the rent datetime.')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @jwt_required
    @api.response(200, 'Rent was successful.')
    @api.response(403, 'User does not have the proper rights.')
    def post(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if not user.is_user_the_owner(user_id):
            rental.create_rental(user_id, kwargs['centre_id'], kwargs['gear_id'], kwargs['rent_amount'],
                                 kwargs['rent_start'], kwargs['rent_end'])
            return {'msg': 'Rent was successful.'}, 200
        return {'msg': 'Permission denied. You are not the user.'}, 403


@ns_gear.route('/getCurrentlyRentedGear')  # for User
class GetCurrentlyRentedGear(Resource):
    resource_fields = api.model('currentlyRentedGear', {
        'centre_id': fields.Integer,
        'centre_name':  fields.String,
        'rent_id': fields.Integer,
        'rent_start': fields.DateTime,
        'rent_end': fields.DateTime,
        'rent_quantity': fields.Integer,
        'gear_name': fields.String
    })

    @jwt_required
    @api.response(200, 'List of currently rented gear by user returned successfully.', [resource_fields])
    def get(self):
        user_id = get_jwt_identity()
        current_gear = gear.get_currently_rented_gear_by_user(user_id)
        return jsonify(current_gear)


@ns_gear.route('/getRentedGear')  # for User
class GetRentedGear(Resource):
    resource_fields = api.model('RentedGear', {
        'centre_id': fields.Integer,
        'centre_name':  fields.String,
        'rent_id': fields.Integer,
        'rent_start': fields.DateTime,
        'rent_end': fields.DateTime,
        'rent_quantity': fields.Integer,
        'gear_name': fields.String
    })

    @jwt_required
    @api.response(200, 'List of rented gear by user returned successfully.', [resource_fields])
    def get(self):
        user_id = get_jwt_identity()
        current_gear = gear.get_rented_gear_by_user(user_id)
        return jsonify(current_gear)


@ns_owner.route('/getCentres')
class GetMyCentres(Resource):
    resource_fields = api.model('getOwnerCentres', {
        "centre_id": fields.Integer,
        "latitude": fields.String,
        "longitude": fields.String,
        "name": fields.String,
        "phone_number": fields.String
    })

    @jwt_required
    @api.response(200, 'List of currently rented gear returned successfully.', [resource_fields])
    def get(self):
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id):
            owner_centres = wc.get_water_centres_by_owner_id(user_id)
            return jsonify(owner_centres)


@ns_rental.route('/getCurrentAndFutureRentals/<int:centre_id>')  # dla wypozyczen trwajacych i przyszlych
class GetRentedGearByCentre(Resource):
    resource_fields = api.model('RentalsByCentreId', {
        'rent_id': fields.Integer,
        'rent_start': fields.DateTime,
        'rent_end': fields.DateTime,
        'rent_quantity': fields.Integer,
        'gear_name': fields.String,
        'gear_id': fields.Integer
    })

    @jwt_required
    @api.response(200, 'List of current and future rentals returned successfully.', [resource_fields])
    def get(self, centre_id):
        current_rentals = rental.get_rentals_by_water_centre_id(centre_id)
        return jsonify(current_rentals)


@ns_owner.route('/getRentalsForCentre/<int:centre_id>')
class GetRentalsForCentre(Resource):
    resource_fields = api.model('RentalsByCentreId', {
        'rent_id': fields.Integer,
        'rent_start': fields.DateTime,
        'rent_end': fields.DateTime,
        'rent_quantity': fields.Integer,
        'gear_name': fields.String,
        'gear_id': fields.Integer,
        'first_name': fields.String,
        'last_name': fields.String,
        'email': fields.String,
        'phone_number': fields.String
    })

    @jwt_required
    @api.response(200, 'List of rentals returned successfully.', [resource_fields])
    def get(self, centre_id):
        user_id = get_jwt_identity()
        if user.is_user_the_owner(user_id) and user.is_owner_the_centre_owner(user_id, centre_id):
            rentals = rental.get_rentals_for_centre_owner(centre_id)
            return jsonify(rentals)


@ns_rental.route('/cancelRent')
class CancelRent(Resource):
    resource_fields = api.model('cancelRent', {
        'rent_id': fields.Integer,
    })
    parser = reqparse.RequestParser()
    parser.add_argument('rent_id', type=int, required=True, help='Rent ID you want to cancel (delete).')

    @api.expect(parser)
    @api.doc(body=resource_fields)
    @api.response(200, 'Cancelation was successful.')
    @api.response(403, 'User does not have the proper rights.')
    @jwt_required
    def delete(self):
        kwargs = self.parser.parse_args(strict=True)
        user_id = get_jwt_identity()
        if rental.is_user_allowed_to_delete_rental(user_id, kwargs['rent_id']):
            rental.delete_rental()
            return {'msg': 'Cancellation was successful.'}, 200
        return {'msg': 'Permission denied. You are not the user nor the owner.'}, 403



# PONIZEJ NIE TYKANE.
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
