/*
 Navicat Premium Data Transfer

 Source Server         : MONGO_LOCAL
 Source Server Type    : MongoDB
 Source Server Version : 80004 (8.0.4)
 Source Host           : localhost:27017
 Source Schema         : microservice_transaction_service

 Target Server Type    : MongoDB
 Target Server Version : 80004 (8.0.4)
 File Encoding         : 65001

 Date: 05/03/2025 01:46:43
*/


// ----------------------------
// Collection structure for transactions
// ----------------------------
db.getCollection("transactions").drop();
db.createCollection("transactions");

// ----------------------------
// Documents of transactions
// ----------------------------
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c0ebd4c5cc83612fd92bb9"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "BANK_ACCOUNT",
    productTypeId: "67bfa9b01226010dfdabfe24",
    transactionType: "DEPOSIT",
    amount: "100",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c0edd8c5cc83612fd92bba"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "CREDIT",
    productTypeId: "67bf901eddc2c72eb4dad76e",
    transactionType: "DEPOSIT",
    amount: "100",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c16b76caa76e0fe8d57ec6"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "BANK_ACCOUNT",
    productTypeId: "67bfa9b01226010dfdabfe24",
    transactionType: "DEPOSIT",
    amount: "100",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c16bb1caa76e0fe8d57ec7"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "BANK_ACCOUNT",
    productTypeId: "67bfa9b01226010dfdabfe24",
    transactionType: "WITHDRAWAL",
    amount: "300",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c1751acaa76e0fe8d57eca"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "CREDIT",
    productTypeId: "67c1712729b272787cfdb624",
    transactionType: "WITHDRAWAL",
    amount: "600",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
db.getCollection("transactions").insert([ {
    _id: ObjectId("67c175b62aee1575164685b8"),
    customerId: "67bc5049652bf702f7552f25",
    productType: "CREDIT",
    productTypeId: "67c1712729b272787cfdb624",
    transactionType: "WITHDRAWAL",
    amount: "600",
    _class: "com.skoy.microservice_transaction_service.model.Transaction"
} ]);
