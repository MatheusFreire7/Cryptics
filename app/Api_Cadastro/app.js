//Imports
require('dotenv').config()
const express = require('express')
const mongoose = require('mongoose')
const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')

const app = express()

app.use(express.json())

app.get('/', (req,res) => {
    res.status(200).json({msg: 'Bem vindo a nossa API' })
})

//Registrar um Usuário
app.post('/auth/register', async(req,res) => {
    const {username, email, senha} = req.body

    //Validações
    if(!username){
        return res.status(422).json({msg: 'O nome é Obrigatório'})
    }

    if(!email){
        return res.status(422).json({msg: 'O email é Obrigatório'})
    }

    if(!senha){
        return res.status(422).json({msg: 'A senha é Obrigatória'})
    }

})

//Credenciais
const dbUser = process.env.DB_USER
const dbSenha = process.env.DB_PASS

mongoose.connect(`mongodb+srv://${dbUser}:${dbSenha}@cluster0.nskuorg.mongodb.net/?retryWrites=true&w=majority`)
    .then(()=>{
    app.listen(3000)
    console.log("Conectou ao Banco de Dados")
})
.catch((err) => console.log(err))






