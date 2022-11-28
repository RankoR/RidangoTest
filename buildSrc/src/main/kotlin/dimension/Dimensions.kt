package dimension

object Dimensions {
    object BuildType {
        object Debug
        object Release
    }


    object Stand {

        const val NAME = "stand"

        object Flavors {
            object Dev {

                const val NAME = "devStand"

                private const val BASE_HOST = "127.0.0.1:31337"

                const val API_BASE_URL = "http://$BASE_HOST/api/v1/"
                const val API_KEY = "TODO"
            }

            object Prod {

                const val NAME = "prodStand"

                private const val BASE_HOST = "localhost"

                const val API_BASE_URL = "http://${BASE_HOST}/api/v1/"
                const val API_KEY = "TODO"
            }
        }
    }

}
