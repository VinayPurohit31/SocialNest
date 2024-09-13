package com.vsp.socialnest.Models

class Post {
    var postUrl: String = ""
    var caption: String = ""
    var uid: String = "" // Changed Uid to uid
    var time: String = ""

    constructor()

    constructor(postUrl: String, caption: String) {
        this.postUrl = postUrl
        this.caption = caption
    }

    constructor(postUrl: String, caption: String, uid: String, time: String) { // Changed Uid to uid
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid
        this.time = time
    }
}