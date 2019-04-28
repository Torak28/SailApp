# Backend

In Progress!

![alt text](https://media.giphy.com/media/UcK7JalnjCz0k/giphy.gif)


### API result:

example for User registration:

```
result: [
            {
                message: "User {username} was created"
                access_token: {access_token}
                refresh_token: {refresh_token}
            }
        ]  
```


### API endpoint:

1. __Main page:__

PATH | METHOD | TOKEN PROTECTION | PURPOSE
-----|--------|------------------|----------
/registerUser | POST | None | User registration
/loginUser | POST | None | User login
/registerOwner | POST | None | Owner registration
/loginUOwner | POST | None | Owner login

2. __Owner panel:__

PATH | METHOD | TOKEN PROTECTION | PURPOSE
-----|--------|------------------|----------
/changeData | POST | Access token | Change some data about Owner(e.g. mail)
/addGear | POST | Access token | Add gear to rental
/deleteGear | POST | Access token | Remove gear from rental
/addGearType | POST | Access token | Add gear type to rental
/deleteGearType | POST | Access token | Remove gear type from rental
/getAllGear | GET | Access token | Get list of all gear
/getGear | GET | Access token | Get gear with specific ID

3. __Client panel:__

PATH | METHOD | TOKEN PROTECTION | PURPOSE
-----|--------|------------------|----------
/changeData | POST | Access token | Change some data about Client(e.g. mail)
/rentGear | POST | Access token | Rent gear
/getAllGear | GET | Access token | Get list of all gear(with some filters e.g. cheapest in given area)
/getGear | GET | Access token | Get gear with specific ID