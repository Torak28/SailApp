from backend.login_register_delete import *

token = login_user('jan.kowalski@test.com', 'testowe_haslo')
print(token)
check_if_token_is_active(token)
print(get_user_data_by_token_value(token))
logout_user(token)
print(check_if_token_is_active(token))
print(get_user_data_by_token_value(token))
