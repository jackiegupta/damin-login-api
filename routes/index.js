var express = require('express');
const ChanceModel = require('./../models/chanceModel');
const User = require('./../models/user');
const jwt = require('jsonwebtoken');

var router = express.Router();

router.use(require('../middlewares/verifyToken'))


router.post('/createChance', async (req, res) => {
  const chanceModel = new ChanceModel({
    phoneNum: req.body.phoneNum,
    customerName: req.body.customerName,
    sex: req.body.sex,
    address: req.body.address,
    service: req.body.service,
    customerSource: req.body.customerSource,
    note: req.body.note,
    createdTime: req.body.createdTime
  });

  await chanceModel.save().then(
    () => res.status(200).send('Create chance success')
  ).catch(err => res.status(400).send(err))

});

router.get('/getAllChance', async (req, res) => {
  ChanceModel.find({}, (err, result) => {
    if (err) {
      res.status(400).send(err)
    } else {
      res.json(result)
    }
  })
})

router.get('/getOneChance/:id', async (req, res) => {
  ChanceModel.findOne({ _id: req.params.id }, (err, result) => {
    if (err) {
      res.status(400).send(err)
    } else {
      res.json(result)
    }
  })
})

module.exports = router;
