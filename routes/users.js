var express = require('express');
var router = express.Router();
var verifyToken = require('../middlewares/verifyToken')

router.post('/' ,verifyToken , (req,res) =>{
  return res.send('Success')
} )
module.exports = router;
