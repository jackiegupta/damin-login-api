const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    phoneNum: {
        type: String,
        required: true,
    },
    customerName: {
        type: String,
        required: true,
    },

    sex: {
        type: String
    },

    address: {
        type: String
    },

    service: {
        type: Array,
        required: true,
    },

    customerSource: {
        type: String
    },
    note: {
        type: String
    },
    createdTime: {
        type: String
    }
});

module.exports = mongoose.model('chanceModel', userSchema);