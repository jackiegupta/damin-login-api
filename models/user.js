const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    email: {
        type: String,
        required: true,
        min: 6,
        max: 225
    },
    password: {
        type: String,
        required: true,
        min: 6,
        max: 255
    },

    refreshToken: {
        type: String
    },
    accessToken: {
        type: String
    }
});

module.exports = mongoose.model('User', userSchema);