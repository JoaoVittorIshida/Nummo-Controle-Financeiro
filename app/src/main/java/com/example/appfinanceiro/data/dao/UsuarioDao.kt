package com.example.appfinanceiro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appfinanceiro.data.entity.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * DAO para a tabela 'usuario'
 */
@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserirUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario")
    fun getTodosUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuario LIMIT 1")
    fun getPrimeiroUsuario(): Flow<Usuario?>
}