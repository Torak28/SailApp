from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
from passlib.hash import sha256_crypt as crypter
from backend.login_register_delete import register_new_person

app = Flask(__name__, static_url_path='')


@app.route('/', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def nazwa_funkcji_niewplywajaca_na_nic():
    print("Request doszedł.")
    return "Rzeczy tutaj są zwracane do jednostki która wysłała zapytanie."


@app.route('/registerUser', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def create_user_account():
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        password = r.get('password')
        phone_number = r.get('phone_number')
        register_new_person(first_name, last_name, email, password, phone_number)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


if __name__ == '__main__':
    app.run(debug=True)
