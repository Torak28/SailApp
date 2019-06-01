package com.pwr.sailapp.data.mocks

import com.pwr.sailapp.data.sail.Centre

object MockCentres {
    val centres = ArrayList<Centre>()
    init{
        centres.add(
            Centre(
                1,
                "Surfpoint",
                4.8,
                "Jastarnia",
                54.692867,
                18.691693,
                "https://www.surfpoint.pl/wp-content/uploads/2015/03/kursy-ins-head-compressor.jpg",
                "511754113"
            )
        )
        centres.add(
            Centre(
                2,
                "Surf Bojo",
                2.4,
                "Jurata",
                54.678396,
                18.717527,
                "https://i.ytimg.com/vi/-OpbV7Kd98M/maxresdefault.jpg",
                "123456789"
            )
        )
        centres.add(
            Centre(
                3,
                "Ważka",
                3.0,
                "Kuchary Żydowskie",
                52.667909,
                20.508873,
                "https://surfmix.com/oferta/surfmix_354110511Aquarius_Trek_2011_2011_01.jpg",
                "511754113"
            )
        )
    }
}