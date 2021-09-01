const jwt = require('jsonwebtoken');

module.exports = (request, response, next) => {
    const token = request.header('Auth-token');
    if (!token) return response.status(401).send('Access Denied');

    try {
        jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, decode) => {
            if (err) {
                console.error(err.toString());
                return response.status(401).json({"error": true, "message": 'Unauthorized access.', err });
            }
            request.decode = decode;
            next();
        });
    } catch (err) {
        return response.status(400).send({"error": true ,'message' : 'Invalid Token'});
    }
};
