package com.codefylabs.Maple.Leaf.rest.ExceptionHandler


class BadApiRequest : RuntimeException {
    constructor(message: String?) : super(message)
    constructor() : super("Bad request api!!")
}
