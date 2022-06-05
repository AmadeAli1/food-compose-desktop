package model

data class UserRegisterForm(
    var name: String,
    var senha: String,
    var email: String,
) {


    constructor() : this("", "", "") {}


}