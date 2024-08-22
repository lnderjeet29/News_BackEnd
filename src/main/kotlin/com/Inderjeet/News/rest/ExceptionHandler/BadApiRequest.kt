package com.Inderjeet.News.rest.ExceptionHandler


class BadApiRequest : RuntimeException {
    constructor(message: String?) : super(message)
    constructor() : super("Bad request !!")
}

