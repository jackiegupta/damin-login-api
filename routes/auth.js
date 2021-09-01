const express = require('express');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const router = express.Router();
const User = require('./../models/user');
const { registerValidator } = require('./../validations/auth');
const e = require('express');

router.post('/signup', async (request, response) => {
    if (!request.body.email || !request.body.password) response.status(422).send('Enter not enough information');
    const { error } = registerValidator(request.body);
    // console.log(request.body);
    if (error) return response.status(422).send('Email is invalid');

    const checkEmailExist = await User.findOne({ email: request.body.email });

    if (checkEmailExist) return response.status(422).send('Email is exist');

    const salt = await bcrypt.genSalt(10);
    const hashPassword = await bcrypt.hash(request.body.password, salt);

    const user = new User({
        email: request.body.email,
        password: hashPassword,
    });

    await user.save().then(newUser => {
        response.status(200).send(newUser);
    }).catch(err => {
        response.status(400).send(err);
    })
});

router.post('/login', async (request, response) => {
    if (!request.body.email || !request.body.password) response.status(422).send('Enter not enough information');
    const user = await User.findOne({ email: request.body.email });
    if (!user) return response.status(401).send("Email doesn't exist");

    const checkPassword = await bcrypt.compare(request.body.password, user.password);

    if (!checkPassword) return response.status(401).send('Password is not correct');

    const userPayload = {
        email: user.email,
        password: user.password
    }
    // Generate AccessToken
    const accessToken = jwt.sign(userPayload, process.env.ACCESS_TOKEN_SECRET, { expiresIn: 60 * 10 });
    // Generate refreshToken
    let refreshToken = jwt.sign(userPayload, process.env.REFRESH_TOKEN_SECRET, { expiresIn: 60 * 60 });
    // Save token in DB
    if (user.refreshToken) {
        refreshToken = user.refreshToken;
    } else {
        user.refreshToken = refreshToken;
        await user.save().then(() => {
            response.status(200).json({ msg: 'Login success!', accessToken, refreshToken })
        }).catch(err => {
            response.status(400).send('Login fail')
        })
    }
    return response.status(200).json({ msg: 'Login success!', accessToken, refreshToken })


})

router.post('/refreshToken', async (req, res) => {
    const accessTokenHeaders = req.header('Auth-token');
    const refreshTokenBody = req.body.refreshToken;
    // console.log('Access', accessTokenHeaders);
    // console.log('Ref', refreshTokenBody);
  
    if (!refreshTokenBody) return res.status(422).send('Dont have refresh token');
  
    const user = await User.findOne({ refreshToken: refreshTokenBody });
    if (!user) return res.status(404).send('User not register');
  
    const accessTokenDecode = jwt.verify(accessTokenHeaders, process.env.ACCESS_TOKEN_SECRET, { ignoreExpiration: true, });
    // console.log(accessTokenDecode);
    if (!accessTokenDecode) return res.status(400).send('AccessToken is invalid')
  
    const email = accessTokenDecode.email;
    // console.log('Email Playload',email);
  
    const checkUserExist = await User.findOne({ email: email })
    // console.log('CheckExist ',checkUserExist);
    if (!checkUserExist) return res.status(400).send('User is not resgister')
    // console.log('RefTkDB ',checkUserExist.refreshToken);
  
    if (refreshTokenBody !== checkUserExist.refreshToken) return res.status(400).send('RefreshToken is invalid')
  
    const userPayload = {
      email: checkUserExist.email,
      password: checkUserExist.password
    }
  
    const accessToken = jwt.sign(userPayload, process.env.ACCESS_TOKEN_SECRET, { expiresIn: 60 * 10 });
    if (!accessToken) return res.status(400).send('Create accessToken fail')
    return res.status(200).send(accessToken)
  })

module.exports = router;