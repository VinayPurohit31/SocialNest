package com.vsp.socialnest.Models

class Pops {
    var popsUrl: String = ""
    var caption: String = ""
    var profileImage: String? =null

    constructor()
    constructor(popsUrl: String, caption: String,) {
        this.popsUrl = popsUrl
        this.caption = caption

    }
    constructor(popsUrl: String, caption: String, profileImage: String) {
        this.popsUrl = popsUrl
        this.caption = caption
        this.profileImage = profileImage
    }
}