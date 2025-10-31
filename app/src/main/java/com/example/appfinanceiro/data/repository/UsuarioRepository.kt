package com.example.appfinanceiro.data.repository


import com.example.appfinanceiro.data.dao.UsuarioDao
import com.example.appfinanceiro.data.entity.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val primeiroUsuario: Flow<Usuario?> = usuarioDao.getPrimeiroUsuario()

    suspend fun inserirUsuario(usuario: Usuario) {
        usuarioDao.inserirUsuario(usuario)
    }

}