package com.pwr.sailapp.data

object MockCentres {
    val centres = ArrayList<Centre>()
    init{
        centres.add(Centre(1,"Surfpoint", 4.8, "Jastarnia", "https://www.surfpoint.pl/wp-content/uploads/2015/03/kursy-ins-head-compressor.jpg", 511754113))
        centres.add(Centre(2,"Surf Bojo", 2.4, "Jurata", "https://i.ytimg.com/vi/-OpbV7Kd98M/maxresdefault.jpg", 123456789))
    }
}