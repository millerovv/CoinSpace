package com.example.alexmelnikov.coinspace.model.entities

/**
 *  Created by Alexander Melnikov on 29.07.18.
 */

class ApiResponseRoot(val timestamp: Long,
                      val base: String,
                      val rates: HashMap<String, Float>)