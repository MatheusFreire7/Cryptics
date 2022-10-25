//Imports
require('dotenv').config()
const express = require('express')
const mongoose = require('mongoose')
const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')

const app = express()

app.use(express.json())

//Modelo User
const User = require('./models/Users')

app.get('/', (req,res) => {
    res.status(200).json({msg: 'Bem vindo a nossa API' })
})

//get do usuário pelo id do banco de dados sendo só possivel para o admin do banco de dados
app.get("/user/:id", async(req,res) =>{
    const id = req.params.id

    //checa se o usuário existe
    const user = await User.findById(id, '-senha')

    if(!user){
        return res.status(404).json({msg: 'Usuário não encontrado'})
    }
    else
        res.status(200).json({user})
})

//Função para validar o email
function validateEmail(email) {
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
  }

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

    if(!validateEmail(email)){
        return res.status(422).json({msg: 'Digite o e-mail no formato correto!'})
    }

    if(!senha){
        return res.status(422).json({msg: 'A senha é Obrigatória'})
    }

    if(!senha.length >= 6){
        return res.status(422).json({msg: 'Digite uma senha com mais de 6 digitos!'})
    }

    //checar se o usuário existe
    const userExiste = await User.findOne({email:email})

    if(userExiste){
        return res.status(422).json({msg: 'E-mail já Existe, por favor utilize outro e-mail'})
    }

    //Criar Senha
    const salt = await bcrypt.genSalt(12)
    const senhaHash = await bcrypt.hash(senha,salt)

    //Criar User
    const user = new User({
        username,
        email,
        senha: senhaHash
    })

 try{
    await user.save()

    res.status(201).json({msg: "Usuario criado com sucesso"})

 } catch(error){
     console.log(error)
    res
    .status(500)
    .json({
        msg: "Aconteceu um erro no Servidor, tente novamente mais tarde!",
    })
 }

})

//Alterar Usuario
app.put('/auth/put/:email', async(req, res) => {
    const email = req.params.email

    //checa se o usuário existe
    const item = await User.findOne({email: email})

    item.email = req.body.email
    item.username = req.body.username
    item.senha = req.body.senha

    const emailNovo = item.email
    const usernameNovo = item.username
    const senhaNova = item.senha

    if(!usernameNovo){
        return res.status(422).json({msg: 'O nome é Obrigatório'})
    }

    if(!emailNovo){
        return res.status(422).json({msg: 'O email é Obrigatório'})
    }

    if(!validateEmail(emailNovo)){
        return res.status(422).json({msg: 'Digite o e-mail no formato correto!'})
    }

    if(!senhaNova){
        return res.status(422).json({msg: 'A senha é Obrigatória'})
    }

    if(!senhaNova.length >= 6){
        return res.status(422).json({msg: 'Digite uma senha com mais de 6 digitos!'})
    }

    //checar se o usuário existe
    const userExiste = await User.findOne({email:emailNovo})

    if(userExiste){
        return res.status(422).json({msg: 'E-mail já Existe, por favor utilize outro e-mail'})
    }

    //Criar Senha
    const salt = await bcrypt.genSalt(12)
    const senhaHash = await bcrypt.hash(senhaNova,salt)

    //Criar User
    const user = new User({
        usernameNovo,
        emailNovo,
        senhaNova: senhaHash
    })


 try{

    User.findOneAndUpdate(email, { username: usernameNovo, email: emailNovo, senha: senhaNova },
                            function (err, docs) {
    if (err){
        console.log(err)
    }
    else{
        res.status(201).json({msg: "Usuario alterado com sucesso"})
        console.log("Updated User : ", docs);
    }
});
    

 } catch(error){
     console.log(error)
    res
    .status(500)
    .json({
        msg: "Aconteceu um erro no Servidor, tente novamente mais tarde!",
    })
 }
 
});

//deletar Usuario
app.delete('/auth/delete/:email', async(req, res) => {
    const email = req.params.email

    try{

        User.findOneAndDelete({email:email},
                                function (err, docs) {
        if (err){
            res.json({msg: "Usuário não encontrado"})
            console.log(err)
        }
        else{
            res.status(201).json({msg: "Usuario excluido com sucesso"})
        }
    });
        
    
     } catch(error){
         console.log(error)
        res
        .status(500)
        .json({
            msg: "Aconteceu um erro no Servidor, tente novamente mais tarde!",
        })
     }
     
});

// Login User
app.post('/auth/login', async (req,res) => {
    const {email,senha} = req.body

    //Validações
    if(!email){
        return res.status(422).json({msg: 'O email é Obrigatório'})
    }

    if(!senha){
        return res.status(422).json({msg: 'A senha é Obrigatória'})
    }

     //checar se o usuário existe
     const user = await User.findOne({email: email})

     if(!user){
         return res.status(422).json({msg: 'Usuário não encontrado'})
     }

     //checar se a senha bate com o email
     const checkSenha = await bcrypt.compare(senha, user.senha)

     if(!checkSenha){
        return res.status(422).json({msg: 'Senha Inválida'})
     }

     try{
      
        const secret = process.env.SECRET

        const token = jwt.sign(
            {
                id: user._id
            },
            secret,
        )
            res.status(200).json({msg: "Autenticação realizado com sucesso", token})
        
     } catch(error){
         console.log(error)
        res
        .status(500)
        .json({
            msg: "Aconteceu um erro no Servidor, tente novamente mais tarde!",
        })
     }
})

//Credenciais
const dbUser = process.env.DB_USER
const dbSenha = process.env.DB_PASS

//Conexão com o mongoDb atlas
mongoose.connect(`mongodb+srv://${dbUser}:${dbSenha}@cluster0.nskuorg.mongodb.net/?retryWrites=true&w=majority`)
    .then(()=>{
    app.listen(3000)
    console.log("Conectou ao Banco de Dados")
})
.catch((err) => console.log(err))






