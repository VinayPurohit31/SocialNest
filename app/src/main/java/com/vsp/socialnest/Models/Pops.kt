package com.vsp.socialnest.Models

class Pops {
    var popsUrl: String = ""
    var caption: String = ""

    constructor()
    constructor(popsUrl: String, caption: String) {
        this.popsUrl = popsUrl
        this.caption = caption
    }
}