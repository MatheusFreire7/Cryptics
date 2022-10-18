const mongoose = require('mongoose')

const User = mongoose.model('User', {
    username: String,
    email: String,
    senha: String
})

module.exports = User