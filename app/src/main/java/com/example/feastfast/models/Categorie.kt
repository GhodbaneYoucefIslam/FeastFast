package com.example.feastfast.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Categorie(
    @PrimaryKey
    val idCategorie :Int ,
    val name : String
)
