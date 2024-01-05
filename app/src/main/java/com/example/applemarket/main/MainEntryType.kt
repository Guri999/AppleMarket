package com.example.applemarket.main

enum class MainEntryType {
    MEMBER,
    NONE
    ;

    companion object{
        fun getEntryType(
            ordinal: Int?
        ): MainEntryType {
            return MainEntryType.values().firstOrNull(){
                it.ordinal == ordinal
            } ?: NONE
        }
    }
}

